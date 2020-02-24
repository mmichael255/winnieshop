package com.winnie;


import com.winnie.common.utils.RsaUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class CommonTest {

    private String pubKeyPath = "D:\\it\\RSAAuth\\rsa_key.pub";
    private String priKeyPath = "D:\\it\\RSAAuth\\rsa_key";

    @Test
    public void rsaCreateKey() throws Exception {
        RsaUtils.generateKey(pubKeyPath,priKeyPath,"heima",2048);
    }

    @Test
    public void rsaGetPubKey() throws Exception {
        PublicKey publicKey = RsaUtils.getPublicKey(pubKeyPath);
        System.out.println(publicKey);
    }
    @Test
    public void rsaGetPriKey() throws Exception {
        PrivateKey privateKey = RsaUtils.getPrivateKey(priKeyPath);
        System.out.println(privateKey);
    }

    @Test
    public void jwtWrite() throws Exception {
        PrivateKey privateKey = RsaUtils.getPrivateKey(priKeyPath);
        String compact = Jwts.builder().setId("boob")
                .signWith(privateKey)
                .setExpiration(new Date(new Date().getTime() + 11111111))
                .claim("role", "admin")
                .compact();

        System.out.println(compact);
        //JhbGciOiJSUzI1NiJ9.eyJqdGkiOiJib29iIiwiZXhwIjoxNTgyMTMzMjM0LCJyb2xlIjoiYWRtaW4ifQ.easkCE_hbyOPq4pLzcgJFeNTLoiUqS9dNpDzzLKbIyCscDzWS7h1YlwHbT1GCEgeIpY795DriIsc_7BlFCUIo_H7Hgyo3irMTiRF65R6bdmjxzQFBQ4IyaRIrAiL3whl3Z0n2oarswtMyU_KVD92Y0b0lorsEG6v0xpCHFNTtJlFbDbMRRgR3EcGBacA6XEocCF0Qqd_VoWFSYFMB1WyzRDVUBXXuFJ5u8XgCnFI0JkWdPIp6JNpbOd-ZSqnO8bKAGZpmlNKXE5f6GLQhVtk0hiWnX3LVzHULyROk4nEct31DHk4QEeSzGWYia1ioZaqJLKxwdk6UlgA4ONO-5uWaw
    }
}
