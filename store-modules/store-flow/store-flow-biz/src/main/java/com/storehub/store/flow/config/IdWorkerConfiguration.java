package com.storehub.store.flow.config;

import com.storehub.store.flow.utils.IdWorker;
import com.storehub.store.flow.utils.IdWorkerIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdWorkerConfiguration {
	@Bean
	public IdWorker idWorker(){
		return new IdWorker();
	}

	@Bean
	public IdWorkerIdGenerator idWorkerIdGenerator() {
		return new IdWorkerIdGenerator();
	}
}
