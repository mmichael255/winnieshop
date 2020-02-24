package com.winnie.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Data
@Configuration
@ConfigurationProperties(prefix = "wn.encoder.crypt")
public class EncoderConfig {
    private String secret;
    private int strength;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        return new BCryptPasswordEncoder(strength,secureRandom);
    }

}
