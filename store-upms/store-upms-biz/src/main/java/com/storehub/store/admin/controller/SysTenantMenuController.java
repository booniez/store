package com.storehub.store.admin.controller;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.storehub.store.admin.api.entity.SysMenu;
import com.storehub.store.admin.api.util.ParamResolver;
import com.storehub.store.admin.service.SysMenuService;
import com.storehub.store.common.core.constant.CommonConstants;
import com.storehub.store.common.core.util.R;
import com.storehub.store.common.log.annotation.SysLog;
import com.storehub.store.admin.api.entity.SysTenantMenu;
import com.storehub.store.admin.service.SysTenantMenuService;
import com.storehub.store.common.mybatis.tenant.TenantBroker;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author store
 * @date 2024-03-18 16:24:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant-menu" )
@Tag(description = "tenantMenu" , name = "管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysTenantMenuController {

    private final SysTenantMenuService sysTenantMenuService;
    private final SysMenuService sysMenuService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysTenantMenu 
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_tenantmenu_view')" )
    public R getSysTenantMenuPage(@ParameterObject Page page, @ParameterObject SysTenantMenu sysTenantMenu) {
        LambdaQueryWrapper<SysTenantMenu> wrapper = Wrappers.lambdaQuery();
        return R.ok(sysTenantMenuService.page(page, wrapper));
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_tenantmenu_view')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(sysTenantMenuService.getById(id));
    }

    /**
     * 新增
     * @param sysTenantMenu 
     * @return R
     */
    @Operation(summary = "新增" , description = "新增" )
    @SysLog("新增" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_tenantmenu_add')" )
    public R save(@RequestBody SysTenantMenu sysTenantMenu) {
        return R.ok(sysTenantMenuService.save(sysTenantMenu));
    }

    /**
     * 修改
     * @param sysTenantMenu 
     * @return R
     */
    @Operation(summary = "修改" , description = "修改" )
    @SysLog("修改" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_tenantmenu_edit')" )
    public R updateById(@RequestBody SysTenantMenu sysTenantMenu) {
        return R.ok(sysTenantMenuService.updateById(sysTenantMenu));
    }

    /**
     * 通过id删除
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除" , description = "通过id删除" )
    @SysLog("通过id删除" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin_tenantmenu_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysTenantMenuService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param sysTenantMenu 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('admin_tenantmenu_export')" )
    public List<SysTenantMenu> export(SysTenantMenu sysTenantMenu, Long[] ids) {
        return sysTenantMenuService.list(Wrappers.lambdaQuery(sysTenantMenu).in(ArrayUtil.isNotEmpty(ids), SysTenantMenu::getId, ids));
    }

	@GetMapping(value = "/tree/menu")
	public R getTree() {
		Long defaultId = ParamResolver.getLong("TENANT_DEFAULT_ID", 1L);
		List<Tree<Long>> trees = new ArrayList<>();
		TenantBroker.runAs(defaultId, (id) -> {
			trees.addAll(sysMenuService.treeMenu(null, null, null));
		});

		return R.ok(trees);
	}

	@GetMapping("/list")
	public R list() {
		List<SysTenantMenu> tenants = sysTenantMenuService.list(
				Wrappers.<SysTenantMenu>lambdaQuery().eq(SysTenantMenu::getStatus, CommonConstants.STATUS_NORMAL));
		return R.ok(tenants);
	}
}