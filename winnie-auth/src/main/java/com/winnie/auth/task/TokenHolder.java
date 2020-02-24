package com.winnie.auth.task;

import com.winnie.auth.config.AuthProperties;
import com.winnie.auth.mapper.ApplicationInfoMapper;
import com.winnie.common.auth.pojo.AppInfo;
import com.winnie.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TokenHolder {

    /**
     * token刷新间隔
     */
    private static final long TOKEN_REFRESH_INTERVAL = 86400000L;
    /**
     * token获取失败后重试的间隔
     */
    private static final long TOKEN_RETRY_INTERVAL = 10000L;

    public String getToken() {
        return token;
    }

    private String token;

    @Autowired
    private AuthProperties prop;
    @Autowired
    private ApplicationInfoMapper applicationInfoMapper;

    @Scheduled(fixedDelay = TOKEN_REFRESH_INTERVAL)
    public void loadTokenTask() throws InterruptedException {
        while (true){
            try {
                List<Long> longs = applicationInfoMapper.selectTargetIDOfApp(prop.getApp().getId());
                AppInfo appInfo = new AppInfo();
                appInfo.setId(prop.getApp().getId());
                appInfo.setServiceName(prop.getApp().getSecret());
                appInfo.setTargetList(longs);
                this.token  = JwtUtils.generateTokenExpireInMinutes(appInfo, prop.getPrivateKey(), prop.getApp().getExpireTime());
                log.info("【认证服务】成功自生成token");
                break;
            }catch (Exception e){
                log.error("【认证服务】失败自生成token");
            }
            Thread.sleep(TOKEN_RETRY_INTERVAL);
        }

    }
}
