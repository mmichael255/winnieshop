package com.winnie.auth.service;

import com.winnie.auth.config.AuthProperties;
import com.winnie.auth.entity.ApplicationInfo;
import com.winnie.auth.mapper.ApplicationInfoMapper;
import com.winnie.common.auth.pojo.AppInfo;
import com.winnie.common.auth.pojo.Payload;
import com.winnie.common.auth.pojo.UserInfo;
import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.common.utils.CookieUtils;
import com.winnie.common.utils.JwtUtils;
import com.winnie.user.UserClient;
import com.winnie.user.entity.User;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private AuthProperties prop;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    private ApplicationInfoMapper applicationInfoMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void login(String username, String password, HttpServletResponse response) {
        try {
            User user = userClient.query(username, password);

            UserInfo userInfo = new UserInfo(user.getId(), user.getUsername(), "admin");

            createTokenToCookie(response, userInfo);
        }catch (Exception e){
            throw new WNException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
    }

    private void createTokenToCookie(HttpServletResponse response, UserInfo userInfo) {
        String s = JwtUtils.generateTokenExpireInMinutes(userInfo, prop.getPrivateKey(), prop.getUser().getExpire());
        CookieUtils.newCookieBuilder()
                .name(prop.getUser().getCookieName())
                .value(s)
                .httpOnly(true)
                .domain(prop.getUser().getCookieDomain())
                .response(response)
                .build();
    }

    public UserInfo verify(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request, prop.getUser().getCookieName());
        Payload<UserInfo> payload =null;
        try {
            payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), UserInfo.class);

        }catch (Exception e){
            throw new WNException(ExceptionEnum.UNAUTHORIZED);
        }
        String id = payload.getId();
        if (redisTemplate.hasKey(id)){
            throw new WNException(ExceptionEnum.UNAUTHORIZED);
        }

        Date expiration = payload.getExpiration();
        DateTime refresh = new DateTime(expiration).minusMinutes(prop.getUser().getRefreshTime());
        UserInfo userInfo = payload.getUserInfo();
        if (refresh.isBeforeNow()){
            createTokenToCookie(response,userInfo);
        }
        return userInfo;
    }

    public void logout(HttpServletRequest request,HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request, prop.getUser().getCookieName());
        Payload<UserInfo> payload =null;
        try{
            payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), UserInfo.class);
            Date expiration = payload.getExpiration();
            long remainTime = expiration.getTime() - System.currentTimeMillis();
            if (remainTime > 3000){
                redisTemplate.opsForValue().set(payload.getId(),"1",remainTime, TimeUnit.MILLISECONDS);
            }

        }catch (Exception e){
         throw new WNException(ExceptionEnum.UNAUTHORIZED);
        }

        CookieUtils.deleteCookie(prop.getUser().getCookieName(),prop.getUser().getCookieDomain(),response);
    }

    public String appAuthorization(Long id, String secret) {
        ApplicationInfo applicationInfo = applicationInfoMapper.selectByPrimaryKey(id);
        if (applicationInfo == null){
            throw new WNException(ExceptionEnum.UNAUTHORIZED);
        }
        String appSecret = applicationInfo.getSecret();

        if (!passwordEncoder.matches(secret,appSecret)){
            throw new WNException(ExceptionEnum.UNAUTHORIZED);
        }

        List<Long> longs = applicationInfoMapper.selectTargetIDOfApp(id);
        AppInfo appInfo = new AppInfo();
        appInfo.setId(id);
        appInfo.setServiceName(applicationInfo.getServiceName());
        appInfo.setTargetList(longs);

        return JwtUtils.generateTokenExpireInMinutes(appInfo, prop.getPrivateKey(), prop.getApp().getExpireTime());
    }
}
