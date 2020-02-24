//package com.winner.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//import org.springframework.web.util.pattern.PathPatternParser;
//
//@Configuration
//@EnableConfigurationProperties(CORSProperties.class)
//public class CORSConfig {
//    @Autowired
//    CORSProperties corsProperties;
//
//    @Bean
//    public CorsWebFilter corsFilter() {
//        //1.添加CORS配置信息
//        CorsConfiguration config = new CorsConfiguration();
//        //1) 允许的域,不要写*，否则cookie就无法使用了
////        config.addAllowedOrigin("http://manage.leyou.com");
////        config.addAllowedOrigin("http://www.leyou.com");
//        corsProperties.getAllowedOrigins().forEach(config::addAllowedOrigin);
//        corsProperties.getAllowedOrigins().forEach(System.out::print);
//
//        //2) 是否发送Cookie信息
//        config.setAllowCredentials(corsProperties.getAllowedCredentials());
//        //3) 允许的请求方式
////        config.addAllowedMethod("OPTIONS");
////        config.addAllowedMethod("HEAD");
////        config.addAllowedMethod("GET");
////        config.addAllowedMethod("PUT");
////        config.addAllowedMethod("POST");
////        config.addAllowedMethod("DELETE");
//        corsProperties.getAllowedMethods().forEach(config::addAllowedMethod);
//        // 4）允许的头信息
////        config.addAllowedHeader("*");
//        corsProperties.getAllowedHeaders().forEach(config::addAllowedHeader);
//        // 5）有效期
//        config.setMaxAge(corsProperties.getMaxAge());
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration(corsProperties.getFilterPath(), config);
//
//        return new CorsWebFilter(source);
//    }
//
//}
