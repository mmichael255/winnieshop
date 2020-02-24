package com.winnie.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "wn.sms")
public class SmsProperties {
    String accessKeyId;
    String accessSecret;
    String regionId;
    String signName;
    String templateCode;
    String sysDomain;
    String sysVersion;
    String sysAction;

}
