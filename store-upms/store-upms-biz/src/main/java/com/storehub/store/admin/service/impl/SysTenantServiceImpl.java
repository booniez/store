package com.storehub.store.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.storehub.store.admin.api.dto.SysTenantDTO;
import com.storehub.store.admin.api.entity.*;
import com.storehub.store.admin.api.util.ParamResolver;
import com.storehub.store.admin.mapper.SysTenantMapper;
import com.storehub.store.admin.service.*;
import com.storehub.store.common.core.constant.CacheConstants;
import com.storehub.store.common.core.constant.CommonConstants;
import com.storehub.store.common.core.util.Pinyin4jUtil;
import com.storehub.store.common.mybatis.tenant.TenantBroker;
import lombok.RequiredArgsConstructor;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author store
 * @date 2024-03-18 16:21:27
 */
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {
	private final SysDictService dictService;
	private final SysDictItemService dictItemService;
	private final SysTenantMenuService tenantMenuService;
	private final SysMenuService menuService;
	private final SysPublicParamService publicParamService;
	private final SysDeptService deptService;
	private final SysUserService userService;
	private final SysTenantUserService tenantUserService;
	private final SysRoleService roleService;
	private final SysUserRoleService userRoleService;
	private final SysRoleMenuService roleMenuService;
	private final SysPublicParamService paramService;
	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.TENANT_DETAILS)
	public Boolean saveTenant(SysTenantDTO tenantDto) {
		SysTenant sysTenant = new SysTenant();
		BeanUtils.copyProperties(tenantDto, sysTenant);
		SysUser existUser = userService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getPhone, tenantDto.getTenantUserPhone()));
		if (!Objects.isNull(existUser)) {
			sysTenant.setTenantUserId(existUser.getUserId());
		} else {
			sysTenant.setTenantUserId(-1L);
		}
		this.save(sysTenant);

		//TODO: 构造租户的字典、字典item、菜单、终端数据、公共参数、角色、用户
		List<SysDict> dictList = new ArrayList<>();
		List<SysDictItem> dictItemList = new ArrayList<>();
		List<SysMenu> menuList = new ArrayList<>();
		List<SysPublicParam> sysPublicParamList = new ArrayList<>();
		Long defaultTenantId = ParamResolver.getLong("TENANT_DEFAULT_ID", 1L);
		TenantBroker.runAs(defaultTenantId, (id) -> {
			dictList.addAll(dictService.list());
			dictItemList.addAll(dictItemService
					.list(Wrappers.<SysDictItem>lambdaQuery()
							.in(SysDictItem::getDictId, dictList.stream()
									.map(SysDict::getId)
									.collect(Collectors.toList()))));

			SysTenantMenu tenantMenuEntity = tenantMenuService.getById(sysTenant.getMenuId());
			String[] menuIds = tenantMenuEntity.getMenuIds().split(",");
			List<SysMenu> sysMenus = menuService.list(Wrappers.<SysMenu>lambdaQuery()
					.in(SysMenu::getMenuId, menuIds));
			menuList.addAll(sysMenus);
			sysPublicParamList.addAll(publicParamService.list());
		});

		return TenantBroker.applyAs(sysTenant.getId(), (id) -> {
			SysDept dept = new SysDept();
			dept.setParentId(0L);
			dept.setName(ParamResolver.getStr("TENANT_DEFAULT_DEPTNAME", "默认部门名称"));
			deptService.save(dept);

			SysTenantUser tenantUserEntity = new SysTenantUser();
			if (sysTenant.getTenantUserId().equals(-1L)) {
				// 创建 base 用户
				SysUser user = new SysUser();
				user.setTenantId(id);
//				user.setUsername(Pinyin4jUtil.convertToPinyin(sysTenant.getName(), HanyuPinyinCaseType.LOWERCASE));
				user.setUsername(tenantDto.getTenantUserPhone());
				user.setPhone(tenantDto.getTenantUserPhone());
				user.setPassword(ENCODER.encode(ParamResolver.getStr("TENANT_DEFAULT_PASSWORD", "123456")));
				userService.save(user);

				sysTenant.setTenantUserId(user.getUserId());
				this.updateById(sysTenant);

				// 创建租户用户
				tenantUserEntity.setUserId(user.getUserId());
			} else {
				tenantUserEntity.setUserId(existUser.getUserId());
			}
			tenantUserEntity.setTenantId(id);
			tenantUserEntity.setDeptId(dept.getDeptId());
			tenantUserEntity.setLockFlag(CommonConstants.STATUS_NORMAL);
			tenantUserService.save(tenantUserEntity);

			//TODO: 菜单、字典、角色等需要完善
			SysRole generalRole = new SysRole();
			generalRole.setRoleName(ParamResolver.getStr("USER_DEFAULT_ROLENAME", "普通用户"));
			generalRole.setRoleCode(ParamResolver.getStr("USER_DEFAULT_ROLECODE", "GENERAL_USER"));
			roleService.save(generalRole);

			SysRole adminRole = new SysRole();
			adminRole.setRoleName(ParamResolver.getStr("TENANT_DEFAULT_ROLENAME", "租户默认角色"));
			adminRole.setRoleCode(ParamResolver.getStr("TENANT_DEFAULT_ROLECODE", "ROLE_ADMIN"));
			roleService.save(adminRole);

			SysUserRole userRole = new SysUserRole();
			userRole.setRoleId(adminRole.getRoleId());
			userRole.setUserId(tenantUserEntity.getId());
			userRoleService.save(userRole);

			List<SysRoleMenu> roleMenus = menuList.stream().map(menu -> {
				SysRoleMenu roleMenu = new SysRoleMenu();
				roleMenu.setMenuId(menu.getMenuId());
				roleMenu.setRoleId(adminRole.getRoleId());
				return roleMenu;
			}).collect(Collectors.toList());
			roleMenuService.saveBatch(roleMenus);
			// 插入系统字典
			dictService.saveBatch(dictList.stream().peek(d -> {
				d.setId(null);
				d.setTenantId(sysTenant.getId());
			}).collect(Collectors.toList()));
			// 处理字典项最新关联的字典ID
			List<SysDictItem> itemList = dictList.stream().flatMap(dict -> dictItemList.stream()
					.filter(item -> item.getDictType().equals(dict.getDictType())).peek(item -> {
						item.setDictId(dict.getId());
						item.setId(null);
						item.setTenantId(sysTenant.getId());
					})).collect(Collectors.toList());

			dictItemService.saveBatch(itemList);
			paramService
					.saveBatch(sysPublicParamList.stream().peek(d -> {
						d.setPublicId(null);
						d.setTenantId(sysTenant.getId());
					}).collect(Collectors.toList()));
			return true;
		});


	}
}