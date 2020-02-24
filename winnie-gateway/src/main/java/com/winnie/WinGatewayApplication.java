package com.winnie;

import com.winnie.config.AuthProperties;
import com.winnie.config.FilterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringCloudApplication
@EnableConfigurationProperties({AuthProperties.class, FilterProperties.class})
@EnableFeignClients
@EnableScheduling
public class WinGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WinGatewayApplication.class,args);
    }
}
