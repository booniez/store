package com.storehub.store.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.storehub.store.admin.api.entity.SysTenantUser;
import com.storehub.store.admin.mapper.SysTenantUserMapper;
import com.storehub.store.admin.service.SysTenantUserService;
import com.storehub.store.common.core.constant.CommonConstants;
import com.storehub.store.common.mybatis.holder.TenantContextHolder;
import com.storehub.store.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 租户用户
 *
 * @author store
 * @date 2024-03-18 16:24:04
 */
@Service
public class SysTenantUserServiceImpl extends ServiceImpl<SysTenantUserMapper, SysTenantUser> implements SysTenantUserService {

	@Override
	public List<Long> getCurrentUserAllTenantIds() {
		TenantContextHolder.setTenantSkip();
		List<SysTenantUser> tenantUsers = baseMapper.selectList(Wrappers.<SysTenantUser>lambdaQuery()
				.eq(SysTenantUser::getUserId, SecurityUtils.getUser().getId())
				.eq(SysTenantUser::getDelFlag, CommonConstants.STATUS_NORMAL)
				.eq(SysTenantUser::getLockFlag, CommonConstants.STATUS_NORMAL)
		);
		return tenantUsers.stream().map(SysTenantUser::getTenantId).collect(Collectors.toList());
	}
}