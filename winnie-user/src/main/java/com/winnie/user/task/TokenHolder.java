package com.winnie.user.task;

import com.winnie.auth.AuthClient;
import com.winnie.user.config.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    private AuthClient client;
    @Autowired
    private AuthProperties prop;

    @Scheduled(fixedDelay = TOKEN_REFRESH_INTERVAL)
    public void loadToken() throws InterruptedException {
        while (true){
            try {
                this.token = client.authorization(prop.getApp().getId(), prop.getApp().getSecret());
                log.info("【用户服务】成功获取token");
                break;
            }catch (Exception e){
                log.error("【用户服务】失败获取token");
            }
            Thread.sleep(TOKEN_RETRY_INTERVAL);
        }
    }
}
