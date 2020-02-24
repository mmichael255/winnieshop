package com.winnie.user.task;

import com.winnie.config.AuthProperties;
import com.winnie.auth.AuthClient;
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
    private AuthProperties prop;

    @Autowired
    private AuthClient client;

    @Scheduled(fixedDelay = TOKEN_REFRESH_INTERVAL)
    public void loadTokenTask() throws InterruptedException {
        while (true){
            try {
                this.token = client.authorization(prop.getApp().getId(),prop.getApp().getSecret());
                log.info("【网关服务】成功从认证得到了token");
                break;
            }catch (Exception e){
                log.error("【网关服务】获取认证失败");
            }
            Thread.sleep(TOKEN_RETRY_INTERVAL);
        }
    }


}
