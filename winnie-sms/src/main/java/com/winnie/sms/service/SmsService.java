package com.winnie.sms.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.winnie.common.constants.SmsConstant;
import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.common.utils.JsonUtils;
import com.winnie.sms.config.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j
@Service
public class SmsService {
    @Autowired
    private IAcsClient client;
    @Autowired
    private SmsProperties prop;

    public void sendVcode(String phone,String code){


        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(prop.getSysDomain());
        request.setSysVersion(prop.getSysVersion());
        request.setSysAction(prop.getSysAction());
        request.putQueryParameter(SmsConstant.SMS_PARAM_REGION_ID, prop.getRegionId());
        request.putQueryParameter(SmsConstant.SMS_PARAM_KEY_PHONE, phone);
        request.putQueryParameter(SmsConstant.SMS_PARAM_KEY_SIGN_NAME, prop.getSignName());
        request.putQueryParameter(SmsConstant.SMS_PARAM_KEY_TEMPLATE_CODE, prop.getTemplateCode());
        request.putQueryParameter(SmsConstant.SMS_PARAM_KEY_TEMPLATE_PARAM, "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            Map<String, String> respMap = JsonUtils.toMap(response.getData(), String.class, String.class);
            if (StringUtils.equals(respMap.get(SmsConstant.SMS_RESPONSE_KEY_CODE),SmsConstant.OK)){
                log.info("【短信发送成功，手机号："+phone+"，验证码："+code);
            } else {
                log.error("短信发送不成功，手机号："+phone+"，验证码："+code);
                throw new WNException(ExceptionEnum.SEND_MESSAGE_ERROR);
            }
        } catch (ServerException e) {
            log.error("【短信验证码发失败】阿里云服务器出了问题！");
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("【短信验证码发失败】阿里云服务器出了问题！");
            e.printStackTrace();
        }
    }
}
