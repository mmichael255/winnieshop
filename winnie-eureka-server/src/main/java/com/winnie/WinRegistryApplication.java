package com.winnie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class WinRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(WinRegistryApplication.class,args);
    }
}
