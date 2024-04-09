package com.storehub.store.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class SysTenantDTO {
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
	private String status;

	/**
	 * 菜单ID
	 */
	@Schema(description="菜单ID")
	private Long menuId;

	/**
	 * 菜单ID
	 */
	@Schema(description="管理员手机号")
	private String tenantUserPhone;
}
