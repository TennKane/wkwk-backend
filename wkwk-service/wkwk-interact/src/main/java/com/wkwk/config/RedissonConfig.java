package com.wkwk.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Configuration
public class RedissonConfig {

    @Value("${redisson.address}")
    private String redissonAddress;

    @Value("${spring.redis.password}")
    private String redissonPassword;

    @Bean
    public RedissonClient redissonClient() {
        // 配置
        Config config = new Config();
        // 地址
        config.useSingleServer().setAddress(redissonAddress).setPassword(redissonPassword);
        config.useSingleServer().setPingConnectionInterval(0);
        // 创建 RedissonClient 对象
        return Redisson.create(config);
    }
}
