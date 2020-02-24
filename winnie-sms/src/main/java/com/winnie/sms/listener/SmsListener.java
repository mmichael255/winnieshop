package com.winnie.sms.listener;

import com.winnie.common.constants.MQConstant;
import com.winnie.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
@Slf4j
@Component
public class SmsListener {
    @Autowired
    private SmsService smsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConstant.Queue.SMS_VERIFY_CODE_QUEUE,durable = "true"),
            exchange = @Exchange(value = MQConstant.Exchange.SMS_EXCHANGE_NAME,type = ExchangeTypes.TOPIC),
            key = MQConstant.RoutingKey.VERIFY_CODE_KEY
    ))
    public void sendVcode(Map<String,String> msgMap){
        if (CollectionUtils.isEmpty(msgMap)){
            log.error("发送信息失败，传输的手机号和验证码为空");
        }

        String phone = msgMap.get("phone");
        String code = msgMap.get("code");
        smsService.sendVcode(phone,code);
    }
}
