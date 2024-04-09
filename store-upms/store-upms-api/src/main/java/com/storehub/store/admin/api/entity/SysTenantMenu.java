package com.storehub.store.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author store
 * @date 2024-03-18 16:24:29
 */
@Data
@TableName("sys_tenant_menu")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class SysTenantMenu extends Model<SysTenantMenu> {

 
	/**
	* id
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="id")
    private Long id;

	/**
	* 租户菜单名称
	*/
    @Schema(description="租户菜单名称")
    private String name;

	/**
	* 菜单id集合
	*/
    @Schema(description="菜单id集合")
    private String menuIds;

	/**
	* 租户菜单,1:冻结,0:正常
	*/
    @Schema(description="租户菜单,1:冻结,0:正常")
    private Integer status;

	/**
	* 创建人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建人")
    private String createBy;

	/**
	* 创建
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建")
    private LocalDateTime createTime;

	/**
	* 修改人
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改人")
    private String updateBy;

	/**
	* 更新时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新时间")
    private LocalDateTime updateTime;
 
	/**
	* delFlag
	*/
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;
}