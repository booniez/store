package com.storehub.store.flow.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.storehub.store.common.core.util.R;
import com.storehub.store.common.log.annotation.SysLog;
import com.storehub.store.flow.api.entity.BizProcessTemplateEntity;
import com.storehub.store.flow.service.BizProcessTemplateService;
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
 * 流程模版
 *
 * @author store
 * @date 2024-03-27 14:44:50
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/process-template" )
@Tag(description = "process-template" , name = "流程模版管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class BizProcessTemplateController {

    private final  BizProcessTemplateService bizProcessTemplateService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param bizProcessTemplate 流程模版
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('flow_process-template_view')" )
    public R getBizProcessTemplatePage(@ParameterObject Page page, @ParameterObject BizProcessTemplateEntity bizProcessTemplate) {
        LambdaQueryWrapper<BizProcessTemplateEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(bizProcessTemplateService.page(page, wrapper));
    }


    /**
     * 通过id查询流程模版
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('flow_process-template_view')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(bizProcessTemplateService.getById(id));
    }

    /**
     * 新增流程模版
     * @param bizProcessTemplate 流程模版
     * @return R
     */
    @Operation(summary = "新增流程模版" , description = "新增流程模版" )
    @SysLog("新增流程模版" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('flow_process-template_add')" )
    public R save(@RequestBody BizProcessTemplateEntity bizProcessTemplate) {
        return R.ok(bizProcessTemplateService.save(bizProcessTemplate));
    }

    /**
     * 修改流程模版
     * @param bizProcessTemplate 流程模版
     * @return R
     */
    @Operation(summary = "修改流程模版" , description = "修改流程模版" )
    @SysLog("修改流程模版" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('flow_process-template_edit')" )
    public R updateById(@RequestBody BizProcessTemplateEntity bizProcessTemplate) {
        return R.ok(bizProcessTemplateService.updateById(bizProcessTemplate));
    }

    /**
     * 通过id删除流程模版
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除流程模版" , description = "通过id删除流程模版" )
    @SysLog("通过id删除流程模版" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('flow_process-template_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(bizProcessTemplateService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param bizProcessTemplate 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('flow_process-template_export')" )
    public List<BizProcessTemplateEntity> export(BizProcessTemplateEntity bizProcessTemplate,Long[] ids) {
        return bizProcessTemplateService.list(Wrappers.lambdaQuery(bizProcessTemplate).in(ArrayUtil.isNotEmpty(ids), BizProcessTemplateEntity::getId, ids));
    }
}