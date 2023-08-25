package com.fishedee.security_boost;

import com.fishedee.security_boost.autoconfig.SecurityBoostProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
public class DefaultWriteTenantAfterAuthSuccessHandler implements WriteTenantAfterAuthSuccessHandler{
    @Autowired
    private SecurityTenantResolver securityTenantResolver;

    @Autowired
    private SecurityBoostProperties properties;

    @Override
    public void onHandle(HttpServletRequest request, HttpServletResponse response){
        String tenantId = securityTenantResolver.getTenantId();
        if( tenantId == null ){
            return;
        }
        //写入cookie
        try{
            String tenantIdEncode = java.net.URLEncoder.encode(tenantId, "UTF-8");
            Cookie nameCookie = new Cookie(properties.getTenantCookieName(), tenantIdEncode);
            //设置Cookie的有效时间，单位为秒
            nameCookie.setMaxAge(properties.getTenantCookieAge());
            nameCookie.setPath("/");
            nameCookie.setHttpOnly(true);
            //通过response的addCookie()方法将此Cookie对象保存到客户端浏览器的Cookie中
            response.addCookie(nameCookie);
        }catch(UnsupportedEncodingException e){
            log.error("{}",e);
        }
    }
}
