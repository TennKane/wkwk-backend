package com.wkwk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableAsync
@EnableFeignClients(basePackages = "com.wkwk.clients")
public class WkwkSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(WkwkSearchApplication.class, args);
    }

}
