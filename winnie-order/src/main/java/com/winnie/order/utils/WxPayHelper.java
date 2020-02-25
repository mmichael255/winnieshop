package com.winnie.order.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConfigImpl;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.order.config.PayConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Component
public class WxPayHelper {
    @Autowired
    private WXPayConfigImpl wxPayConfig;

    @Autowired
    private WXPay wxPay;

    public String getWxPayUrl(Long orderId, Long totalFee){
        log.info("【获取微信支付链接】工具类开始调用！");
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "乐优商城");
        data.put("out_trade_no", orderId.toString());
        data.put("total_fee", totalFee.toString());
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", wxPayConfig.getNotifyUrl());
        data.put("trade_type", wxPayConfig.getPayType());  // 此处指定为扫码支付

        try {
            Map<String, String> responseMap = wxPay.unifiedOrder(data);
            checkPayStatus(responseMap);
            log.info("【获取微信支付链接】工具类成功结束！");
            return responseMap.get("code_url");
        } catch (Exception e) {
            throw new WNException(501, "【获取微信支付链接】失败！");
        }
    }

    public void checkPayStatus(Map<String, String> responseMap) {
        if (!StringUtils.equals(responseMap.get("return_code"),"SUCCESS")){
            log.error("【获取微信支付链接】通信失败！异常信息为：{}", responseMap.get("return_msg"));
            throw new WNException(501, "【获取微信支付链接】失败！");
        }
        if (!StringUtils.equals(responseMap.get("result_code"),"SUCCESS")){
            log.error("【获取微信支付链接】通信失败！异常信息为：{}", responseMap.get("err_code_des"));
            throw new WNException(501, "【获取微信支付链接】失败！");
        }
    }
}
