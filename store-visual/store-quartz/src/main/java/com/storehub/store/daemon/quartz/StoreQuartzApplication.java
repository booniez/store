package com.storehub.store.daemon.quartz;

import com.storehub.store.common.feign.annotation.EnableStoreFeignClients;
import com.storehub.store.common.security.annotation.EnableStoreResourceServer;
import com.storehub.store.common.swagger.annotation.EnableStoreDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author frwcloud
 * @date 2023-07-05
 */
@EnableStoreDoc("job")
@EnableStoreFeignClients
@EnableStoreResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class StoreQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreQuartzApplication.class, args);
	}

}
