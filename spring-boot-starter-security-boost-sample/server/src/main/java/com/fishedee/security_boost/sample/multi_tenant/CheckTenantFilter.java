package com.fishedee.security_boost.sample.multi_tenant;

import com.fishedee.security_boost.DefaultUserDetail;
import com.fishedee.security_boost.autoconfig.SecurityBoostProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CheckTenantFilter implements HandlerInterceptor {

    @Autowired
    private SecurityBoostProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler) throws Exception {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl)httpRequest.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        String tenantId = MyTenantHolder.getTenantId();
        if( securityContextImpl != null ){
            AbstractAuthenticationToken token = (AbstractAuthenticationToken) securityContextImpl.getAuthentication();
            DefaultUserDetail userDetail = (DefaultUserDetail)token.getPrincipal();
            if( tenantId != null && tenantId.equals(userDetail.getTenantId()) == false ){
                //清空tenant的Cookie
                Cookie newCookie = new Cookie(properties.getTenantCookieName(),null);
                newCookie.setMaxAge(properties.getTenantCookieAge());
                newCookie.setPath("/");
                httpResponse.addCookie(newCookie);

                //log
                String msg = String.format("tenant not equal %s != %s",
                        tenantId,
                        userDetail.getTenantId());
                log.error("uri:{} , error {}",httpRequest.getRequestURI(),msg);

                //返回报错
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("application/json");
                String data = "{\"code\":1,\"msg\":\"登录过期\"}";
                httpResponse.getWriter().write(data);
                httpResponse.getWriter().flush();
                return false;
            }
        }
        return true;
    }
}
