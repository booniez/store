package com.storehub.store.flow;

import com.storehub.store.common.feign.annotation.EnableStoreFeignClients;
import com.storehub.store.common.security.annotation.EnableStoreResourceServer;
import com.storehub.store.common.swagger.annotation.EnableStoreDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
* @author pig archetype
* <p>
* 项目启动类
*/
@EnableStoreDoc(value = "store")
@EnableStoreFeignClients
@EnableStoreResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class FlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowApplication.class, args);
    }

}
