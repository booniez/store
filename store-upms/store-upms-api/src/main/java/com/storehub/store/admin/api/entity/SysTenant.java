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
 * @date 2024-03-18 16:21:27
 */
@Data
@TableName("sys_tenant")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class SysTenant extends Model<SysTenant> {


	/**
	* 租户ID
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="租户ID")
    private Long id;

	/**
	* 父租户ID
	*/
    @Schema(description="父租户ID")
    private Long parentId;

	/**
	* 租户名称
	*/
    @Schema(description="租户名称")
    private String name;

	/**
	* 租户编码
	*/
    @Schema(description="租户编码")
    private String code;

	/**
	* 租户域名
	*/
    @Schema(description="租户域名")
    private String tenantDomain;

	/**
	* 账号数量
	*/
    @Schema(description="账号数量")
    private Integer accountCount;

	/**
	* 租户开始时间
	*/
    @Schema(description="租户开始时间")
    private LocalDateTime startTime;

	/**
	* 租户结束时间
	*/
    @Schema(description="租户结束时间")
    private LocalDateTime endTime;

	/**
	* 租户状态，0正常，1停用
	*/
    @Schema(description="租户状态，0正常，1停用")
    private Integer status;

	/**
	* 菜单ID
	*/
    @Schema(description="菜单ID")
    private Long menuId;

	/**
	* 租户管理员用户ID
	*/
    @Schema(description="租户管理员用户ID")
    private Long tenantUserId;

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
	* 更新时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新时间")
    private LocalDateTime updateTime;

	/**
	* 删除标记，0未删除，1已删除
	*/
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;
}