package com.storehub.store.admin.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.storehub.store.common.core.util.R;
import com.storehub.store.common.log.annotation.SysLog;
import com.storehub.store.admin.api.entity.SysTenantUser;
import com.storehub.store.admin.service.SysTenantUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户用户
 *
 * @author store
 * @date 2024-03-18 16:24:04
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tenantUser" )
@Tag(description = "tenantUser" , name = "租户用户管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysTenantUserController {

    private final  SysTenantUserService sysTenantUserService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysTenantUser 租户用户
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_tenantUser_view')" )
    public R getSysTenantUserPage(@ParameterObject Page page, @ParameterObject SysTenantUser sysTenantUser) {
        LambdaQueryWrapper<SysTenantUser> wrapper = Wrappers.lambdaQuery();
        return R.ok(sysTenantUserService.page(page, wrapper));
    }


    /**
     * 通过id查询租户用户
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_tenantUser_view')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(sysTenantUserService.getById(id));
    }

    /**
     * 新增租户用户
     * @param sysTenantUser 租户用户
     * @return R
     */
    @Operation(summary = "新增租户用户" , description = "新增租户用户" )
    @SysLog("新增租户用户" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_tenantUser_add')" )
    public R save(@RequestBody SysTenantUser sysTenantUser) {
        return R.ok(sysTenantUserService.save(sysTenantUser));
    }

    /**
     * 修改租户用户
     * @param sysTenantUser 租户用户
     * @return R
     */
    @Operation(summary = "修改租户用户" , description = "修改租户用户" )
    @SysLog("修改租户用户" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_tenantUser_edit')" )
    public R updateById(@RequestBody SysTenantUser sysTenantUser) {
        return R.ok(sysTenantUserService.updateById(sysTenantUser));
    }

    /**
     * 通过id删除租户用户
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除租户用户" , description = "通过id删除租户用户" )
    @SysLog("通过id删除租户用户" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin_tenantUser_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysTenantUserService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param sysTenantUser 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('admin_tenantUser_export')" )
    public List<SysTenantUser> export(SysTenantUser sysTenantUser, Long[] ids) {
        return sysTenantUserService.list(Wrappers.lambdaQuery(sysTenantUser).in(ArrayUtil.isNotEmpty(ids), SysTenantUser::getId, ids));
    }
}