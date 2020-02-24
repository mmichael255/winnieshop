package com.winnie.filter;

import com.winnie.config.AuthProperties;
import com.winnie.user.task.TokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Slf4j
@Component
public class PrivilegeFilter implements GlobalFilter, Ordered {
    @Autowired
    private TokenHolder tokenHolder;
    @Autowired
    private AuthProperties authProperties;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = tokenHolder.getToken();
        ServerHttpRequest host = exchange.getRequest().mutate().header(authProperties.getApp().getHeaderName(), token).build();
        ServerWebExchange build = exchange.mutate().request(host).build();

        return chain.filter(build);
    }

    @Override
    public int getOrder() {
        return 6;
    }
}
