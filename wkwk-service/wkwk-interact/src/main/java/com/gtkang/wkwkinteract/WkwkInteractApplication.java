package com.gtkang.wkwkinteract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.gtkang.feignapi.clients")
public class WkwkInteractApplication {

    public static void main(String[] args) {
        SpringApplication.run(WkwkInteractApplication.class, args);
    }

}
