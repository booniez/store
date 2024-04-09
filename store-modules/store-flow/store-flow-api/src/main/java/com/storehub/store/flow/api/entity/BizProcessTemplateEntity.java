package com.storehub.store.flow.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 流程模版
 *
 * @author store
 * @date 2024-03-27 14:44:50
 */
@Data
@TableName("biz_process_template")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "流程模版")
public class BizProcessTemplateEntity extends Model<BizProcessTemplateEntity> {

 
	/**
	* id
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="id")
    private Long id;

	/**
	* 模型名称
	*/
    @Schema(description="模型名称")
    private String name;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

    /**
	* 模型JSON
	*/
    @Schema(description="模型JSON")
    private String templateJson;

	/**
	* 创建人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建人")
    private String createBy;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

	/**
	* 修改人
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改人")
    private String updateBy;

	/**
	* 修改时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改时间")
    private LocalDateTime updateTime;

	/**
	* 删除标志
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="删除标志")
    private String delFlag;

	/**
	* 租户ID
	*/
    @Schema(description="租户ID")
    private Long tenantId;
}