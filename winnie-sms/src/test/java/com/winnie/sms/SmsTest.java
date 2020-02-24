package com.winnie.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.winnie.common.constants.MQConstant;
import com.winnie.common.constants.SmsConstant;
import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.common.utils.JsonUtils;
import com.winnie.sms.config.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsTest {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Test
    public void sendTest(){
        HashMap<String, String> map = new HashMap<>();
        map.put("phone","15502072338");
        map.put("code","9999");
        amqpTemplate.convertAndSend(MQConstant.Exchange.SMS_EXCHANGE_NAME,
                MQConstant.RoutingKey.VERIFY_CODE_KEY,
                map
        );
    }

//    @Autowired
//    private IAcsClient client;

}
