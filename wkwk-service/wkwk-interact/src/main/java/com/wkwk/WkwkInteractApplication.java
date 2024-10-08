package com.wkwk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.wkwk.clients")
@EnableTransactionManagement
public class WkwkInteractApplication {

    public static void main(String[] args) {
        SpringApplication.run(WkwkInteractApplication.class, args);

    }

}
