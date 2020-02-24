package com.winnie.filter;

import com.winnie.common.constants.WNConstant;
import com.winnie.config.AuthProperties;
import com.winnie.config.FilterProperties;
import com.winnie.common.auth.pojo.Payload;
import com.winnie.common.auth.pojo.UserInfo;
import com.winnie.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthProperties prop;
    @Autowired
    private FilterProperties filterProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isAllowPaths(request)){
            return chain.filter(exchange);
        }
        HttpCookie cookie = request.getCookies().getFirst(prop.getUser().getCookieName());
        String token = cookie.getValue();
        if (cookie == null){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        Payload<UserInfo> payload =null;
        try{
            payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), UserInfo.class);
            UserInfo user = payload.getUserInfo();
            // TODO: 做权限校验
            String role = user.getRole();
            System.out.println("角色======>"+role);
            String path = request.getURI().getPath();
            System.out.println("路径======> "+path);
            String method = request.getMethodValue();
            System.out.println("方法类型：=====>"+method);
            Long id = user.getId();
            ServerHttpRequest build = exchange.getRequest().mutate().header(WNConstant.USER_ID_HEADER, id.toString()).build();
            ServerWebExchange exchange1 = exchange.mutate().request(build).build();

            // TODO 判断权限，此处暂时空置，等待权限服务完成后补充
            log.info("【网关】用户{},角色{}。访问服务{} : {}，", user.getUsername(), role, method, path);
            return chain.filter(exchange1);
        }catch (Exception e){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }


    }

    private boolean isAllowPaths(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        List<String> allowPaths = filterProperties.getAllowPaths();
        for (String allowPath : allowPaths) {
            if (path.startsWith(allowPath)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
