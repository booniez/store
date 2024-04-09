package com.storehub.store.common.mybatis.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.storehub.store.common.mybatis.config.TenantConfigProperties;
import com.storehub.store.common.mybatis.holder.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class TenantHandler implements TenantLineHandler {
	private final TenantConfigProperties properties;


	@Override
	public Expression getTenantId() {
		Long tenantId = TenantContextHolder.getTenantId();
		if (Objects.isNull(tenantId)) {
			return new NullValue();
		}
		return new LongValue(tenantId);
	}

	@Override
	public String getTenantIdColumn() {
		return properties.getColumn();
	}

	@Override
	public boolean ignoreTable(String tableName) {
		if (TenantContextHolder.getTenantSkip()) {
			return Boolean.TRUE;
		}
		Long tenantId = TenantContextHolder.getTenantId();
		// 不传递租户则查询所有数据
		if (Objects.isNull(tenantId)) {
			return Boolean.TRUE;
		}

		return !properties.getTables().contains(tableName);
	}
}
