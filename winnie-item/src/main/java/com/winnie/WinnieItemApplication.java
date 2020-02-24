package com.winnie;

import com.sun.glass.ui.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.winnie.item.mapper")
public class WinnieItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(WinnieItemApplication.class,args);
    }
}
