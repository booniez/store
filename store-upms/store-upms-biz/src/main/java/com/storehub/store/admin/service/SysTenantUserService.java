package com.storehub.store.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.storehub.store.admin.api.entity.SysTenantUser;

import java.util.List;

public interface SysTenantUserService extends IService<SysTenantUser> {
	List<Long> getCurrentUserAllTenantIds();
}