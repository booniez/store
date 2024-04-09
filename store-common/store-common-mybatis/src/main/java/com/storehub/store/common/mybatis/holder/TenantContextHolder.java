package com.storehub.store.common.mybatis.holder;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.storehub.store.common.mybatis.constant.TenantContexConstants;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class TenantContextHolder {
	public final ThreadLocal<Map<String, Object>> threadLocal = new TransmittableThreadLocal<Map<String, Object>>();

	private final int threadLocalConstantsLength = TenantContexConstants.class.getDeclaredFields().length;

	public void set(String key, Object value) {
		Map<String, Object> map = threadLocal.get();
		if (map == null) {
			map = new HashMap<String, Object>(threadLocalConstantsLength);
			threadLocal.set(map);
		}
		map.put(key, value);
	}

	public Object get(String key) {
		Map<String, Object> map = threadLocal.get();
		if (map == null) {
			map = new HashMap<String, Object>(threadLocalConstantsLength);
			threadLocal.set(map);
		}
		return map.get(key);
	}

	public void setTenantId(Long tenantId) {
		set(TenantContexConstants.THREAD_LOCAL_TENANT, tenantId);
	}

	public Long getTenantId() {
		Object tenantId = get(TenantContexConstants.THREAD_LOCAL_TENANT);
		return tenantId == null ? null : Long.valueOf(String.valueOf(tenantId));
	}

	public void setTenantSkip() {
		set(TenantContexConstants.THREAD_LOCAL_TENANT_SKIP_FLAG, Boolean.TRUE);
	}

	public Boolean getTenantSkip() {
		Object tenantSkip = get(TenantContexConstants.THREAD_LOCAL_TENANT_SKIP_FLAG);
		return tenantSkip != null && (Boolean) tenantSkip;
	}

	public void clear() {
		Map<String, Object> map = threadLocal.get();
		map.clear();
		threadLocal.set(map);
	}

}
