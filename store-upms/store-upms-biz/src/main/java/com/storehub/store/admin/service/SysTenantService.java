package com.storehub.store.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.storehub.store.admin.api.dto.SysTenantDTO;
import com.storehub.store.admin.api.entity.SysTenant;

public interface SysTenantService extends IService<SysTenant> {
	/**
	 * 新增租户
	 * @param sysTenant
	 * @return
	 */
	Boolean saveTenant(SysTenantDTO sysTenant);
}