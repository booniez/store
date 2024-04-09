package com.storehub.store.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 租户用户
 *
 * @author store
 * @date 2024-03-18 16:24:04
 */
@Data
@TableName("sys_tenant_user")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "租户用户")
public class SysTenantUser extends Model<SysTenantUser> {


	/**
	* 租户用户ID
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="租户用户ID")
    private Long id;

	/**
	* base 用户ID
	*/
    @Schema(description="base 用户ID")
    private Long userId;

	/**
	* 0-正常，9-锁定
	*/
    @Schema(description="0-正常，9-锁定")
    private String lockFlag;

	/**
	* 所属部门ID
	*/
    @Schema(description="所属部门ID")
    private Long deptId;

	/**
	* 创建者
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建者")
    private String createBy;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

	/**
	* 更新人
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新人")
    private String updateBy;

	/**
	* 修改时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改时间")
    private LocalDateTime updateTime;

	/**
	* 0-正常，1-删除
	*/
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

	/**
	* 租户ID
	*/
    @Schema(description="租户ID")
    private Long tenantId;

    @TableField(exist = false)
    private String tenantName;
}