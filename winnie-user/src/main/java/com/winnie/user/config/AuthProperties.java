package com.winnie.user.config;

import com.winnie.common.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "wn.jwt")
public class AuthProperties {

    private String pubKeyPath;

    private PublicKey publicKey;

    private JwtUser user = new JwtUser();

    private App app = new App();
    @Data
    public class JwtUser {
        private String cookieName;
    }

    @Data
    public class App{
        private Long id;
        private String secret;
        private String headerName;
    }

    @PostConstruct
    public void initMethod() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
    }
}
