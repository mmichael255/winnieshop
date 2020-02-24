package com.winnie.sms.config;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfig {


    @Bean
    public IAcsClient acsClient(SmsProperties prop){
        DefaultProfile profile = DefaultProfile.getProfile(prop.getRegionId(), prop.getAccessKeyId(), prop.getAccessSecret());
        return new DefaultAcsClient(profile);

//        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FqZrR75q3ggR7eu3aGx", "6QihAbeIeIYJruea8QH27LCM939tH5");
//        return new DefaultAcsClient(profile);
    }
}
