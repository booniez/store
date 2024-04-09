package com.storehub.store.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import com.storehub.store.admin.api.dto.SysTenantDTO;
import com.storehub.store.admin.api.entity.SysTenant;
import com.storehub.store.admin.service.SysTenantService;
import com.storehub.store.common.core.util.R;
import com.storehub.store.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 
 *
 * @author store
 * @date 2024-03-18 16:21:27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant" )
@Tag(description = "tenant" , name = "管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysTenantController {

    private final  SysTenantService sysTenantService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysTenant 
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_tenant_view')" )
    public R getSysTenantPage(@ParameterObject Page page, @ParameterObject SysTenant sysTenant) {
        LambdaQueryWrapper<SysTenant> wrapper = Wrappers.lambdaQuery();
        return R.ok(sysTenantService.page(page, wrapper));
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_tenant_view')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(sysTenantService.getById(id));
    }

    /**
     * 新增
     * @param tenantDto
     * @return R
     */
    @Operation(summary = "新增" , description = "新增" )
    @SysLog("新增" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_tenant_add')" )
	public R save(@RequestBody SysTenantDTO tenantDto) {
		return R.ok(sysTenantService.saveTenant(tenantDto));
	}

    /**
     * 修改
     * @param sysTenant 
     * @return R
     */
    @Operation(summary = "修改" , description = "修改" )
    @SysLog("修改" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_tenant_edit')" )
    public R updateById(@RequestBody SysTenant sysTenant) {
        return R.ok(sysTenantService.updateById(sysTenant));
    }

    /**
     * 通过id删除
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除" , description = "通过id删除" )
    @SysLog("通过id删除" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin_tenant_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysTenantService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param sysTenant 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('admin_tenant_export')" )
    public List<SysTenant> export(SysTenant sysTenant, Long[] ids) {
        return sysTenantService.list(Wrappers.lambdaQuery(sysTenant).in(ArrayUtil.isNotEmpty(ids), SysTenant::getId, ids));
    }
}