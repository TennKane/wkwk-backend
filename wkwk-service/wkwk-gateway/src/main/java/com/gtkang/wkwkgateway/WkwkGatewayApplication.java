package com.gtkang.wkwkgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动类

 */

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class WkwkGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WkwkGatewayApplication.class, args);
    }

}
