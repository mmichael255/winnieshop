package com.winnie.interceptors;

import com.winnie.common.auth.pojo.AppInfo;
import com.winnie.common.auth.pojo.Payload;
import com.winnie.common.auth.pojo.UserInfo;
import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.common.utils.JwtUtils;
import com.winnie.user.config.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
public class MVCPrivilegeInterceptor implements HandlerInterceptor {
    private AuthProperties prop;

    public MVCPrivilegeInterceptor(AuthProperties prop) {
        this.prop = prop;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String token = request.getHeader(prop.getApp().getHeaderName());
            Payload<AppInfo> payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), AppInfo.class);
            AppInfo appInfo = payload.getUserInfo();
            List<Long> targetList = appInfo.getTargetList();
            Long id = prop.getApp().getId();
            if (targetList == null || !targetList.contains(id)){
                log.error("请求者没有访问本服务的权限！");
            }

            log.info("{}服务正在请求资源：{}服务",appInfo.getServiceName(),prop.getApp().getSecret());
            return true;
        }catch (Exception e){
            log.error("服务访问被拒绝,token认证失败!", e);
            return false;
        }
    }
}
