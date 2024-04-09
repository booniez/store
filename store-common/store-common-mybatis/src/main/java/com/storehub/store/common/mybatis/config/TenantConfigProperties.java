package com.storehub.store.common.mybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "store.tenant")
public class TenantConfigProperties {
	/**
	 * mybatis plus 插件中对应字段
	 */
	private String column = "tenant_id";
	/**
	 * 多租户的表集合
	 */
	private List<String> tables = new ArrayList<>();
}
