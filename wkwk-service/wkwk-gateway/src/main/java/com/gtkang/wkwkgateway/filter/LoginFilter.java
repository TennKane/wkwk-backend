package com.gtkang.wkwkgateway.filter;


import com.wkwk.constant.UserConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 登录过滤器
 */
@Order(0)
@Component
@Log4j2
public class LoginFilter implements GlobalFilter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 过滤器
     * @param exchange 请求上下文
     * @param chain 过滤器链
     * @return 过滤结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        RequestPath path = exchange.getRequest().getPath();
        // 如果是登录请求 或者 注册请求，直接放行
        if (path.toString().contains("/login")) {
            return chain.filter(exchange);
        }
        // 如果是其他请求，判断是否已经登录
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        String userId = stringRedisTemplate.opsForValue().get(UserConstant.REDIS_LOGIN_TOKEN + token);
        if (userId != null) {
            // 刷新token有效期
            stringRedisTemplate.opsForValue().set(UserConstant.REDIS_LOGIN_TOKEN + token, userId,
                    UserConstant.LOGIN_USER_TTL, java.util.concurrent.TimeUnit.SECONDS);
            return chain.filter(exchange);
        }
        // 如果未登录，返回错误信息
        log.info("拦截到未登录请求:{}", path.toString());
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
