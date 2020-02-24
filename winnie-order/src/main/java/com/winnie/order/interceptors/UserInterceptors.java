package com.winnie.order.interceptors;

import com.winnie.common.auth.pojo.UserHolder;
import com.winnie.common.constants.WNConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIdStr = request.getHeader(WNConstant.USER_ID_HEADER);
        UserHolder.setUserId(Long.valueOf(userIdStr));
        log.info("【订单微服务】从网关的请求头中获取到了当前的用户id为："+userIdStr);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUserId();
    }
}
