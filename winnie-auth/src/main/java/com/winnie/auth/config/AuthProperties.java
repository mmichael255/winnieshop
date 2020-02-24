package com.winnie.auth.config;

import com.winnie.common.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "wn.jwt")
public class AuthProperties {

    private String pubKeyPath;
    private String priKeyPath;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    private JwtUser user = new JwtUser();

    private App app = new App();
    @Data
    public class JwtUser {
        private Integer expire;
        private Integer refreshTime;
        private String cookieName;
        private String cookieDomain;

    }
    @Data
    public class App {
        private Integer expireTime;
        private Long id;
        private String secret;
        private String headerName;
    }

    @PostConstruct
    public void initMethod() throws Exception {
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
    }
}
