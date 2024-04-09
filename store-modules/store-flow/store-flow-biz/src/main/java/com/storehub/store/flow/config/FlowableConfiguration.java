package com.storehub.store.flow.config;

import cn.hutool.core.collection.ListUtil;
import com.storehub.store.flow.handler.CustomJobHandler;
import com.storehub.store.flow.utils.IdWorker;
import com.storehub.store.flow.utils.IdWorkerIdGenerator;
import lombok.RequiredArgsConstructor;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.cfg.HttpClientConfig;
import org.flowable.engine.impl.cfg.DelegateExpressionFieldInjectionMode;
import org.flowable.job.service.JobHandler;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class FlowableConfiguration {
	private final PlatformTransactionManager transactionManager;
	private final IdWorkerIdGenerator idWorkerIdGenerator;
	/**
	 * BPM 模块的 ProcessEngineConfigurationConfigurer 实现类：
	 *
	 * 1. 设置各种监听器
	 * 2. 设置自定义的 ActivityBehaviorFactory 实现
	 */
	@Bean
	public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> bpmProcessEngineConfigurationConfigurer(
			ObjectProvider<FlowableEventListener> listeners) {
		return configuration -> {
			// 注册监听器，例如说 BpmActivityEventListener
			configuration.setEventListeners(ListUtil.toList(listeners.iterator()));
			configuration.setDisableEventRegistry(Boolean.TRUE);
			configuration.setActivityFontName("宋体");
			configuration.setAnnotationFontName("宋体");
			configuration.setLabelFontName("黑体");
			configuration.setTransactionManager(transactionManager);
			configuration.setDisableIdmEngine(Boolean.TRUE);
			configuration.setDelegateExpressionFieldInjectionMode(DelegateExpressionFieldInjectionMode.MIXED);
			configuration.setIdGenerator(idWorkerIdGenerator);
			configuration.setAsyncExecutorActivate(Boolean.TRUE);
			HttpClientConfig httpClientConfig=new HttpClientConfig();
			//连接超时
			httpClientConfig.setConnectTimeout(1000000);
			//连接请求超时
			httpClientConfig.setConnectionRequestTimeout(1000000);
			//双端建立socket连接
			httpClientConfig.setSocketTimeout(1000000);
			//请求失败之后重试次数
			httpClientConfig.setRequestRetryLimit(3);
			configuration.setHttpClientConfig(httpClientConfig);
			configuration.setKnowledgeBaseCacheLimit(200);
			configuration.setProcessDefinitionCacheLimit(200);
			List<JobHandler> customJobHandlers =new ArrayList<>();
			customJobHandlers.add(new CustomJobHandler());
			configuration.setCustomJobHandlers(customJobHandlers);
		};
	}
}
