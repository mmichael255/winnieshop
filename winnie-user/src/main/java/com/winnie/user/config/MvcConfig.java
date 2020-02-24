package com.winnie.user.config;

import com.winnie.interceptors.MVCPrivilegeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.swing.undo.AbstractUndoableEdit;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private AuthProperties prop;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MVCPrivilegeInterceptor(prop)).excludePathPatterns("/swagger-ui.html");
    }
}
