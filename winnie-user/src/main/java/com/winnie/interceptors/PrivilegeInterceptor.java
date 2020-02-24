package com.winnie.interceptors;

import com.winnie.user.config.AuthProperties;
import com.winnie.user.task.TokenHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrivilegeInterceptor implements RequestInterceptor{
    @Autowired
    private TokenHolder tokenHolder;
    @Autowired
    private AuthProperties prop;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = tokenHolder.getToken();
        requestTemplate.header(prop.getApp().getHeaderName(),token);
    }


}
