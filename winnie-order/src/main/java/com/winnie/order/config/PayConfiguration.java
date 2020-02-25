package com.winnie.order.config;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfiguration {
    /**
     * @ConfigurationProperties写到方法上
     * 方法内需要new的对象中的属性名只要和配置文件中的名称一致，可以直接构建对象使用
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "wn.pay")
    public WXPayConfigImpl wxPayConfig(){
        return new WXPayConfigImpl();
    }

    @Bean
    public WXPay wxPay(WXPayConfigImpl wxPayConfig) throws Exception {
        return new WXPay(wxPayConfig);
    }
}
