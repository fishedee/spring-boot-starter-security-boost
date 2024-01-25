package com.fishedee.security_boost;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RememberMeSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Autowired
    private WriteTenantAfterAuthSuccessHandler writeTenantAfterAuthSuccessHandler;

    private HttpSecurity httpSecurity;

    private SessionAuthenticationStrategy sessionStrategy;

    public void setHttpSecurity(HttpSecurity httpSecurity){
        this.httpSecurity = httpSecurity;
    }

    public  SessionAuthenticationStrategy getSessionStrategy(){
        if( sessionStrategy != null ){
            return this.sessionStrategy;
        }
        synchronized(this){
            this.sessionStrategy = httpSecurity.getSharedObject(SessionAuthenticationStrategy.class);
            return this.sessionStrategy;
        }

    }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event){
        if( RememberMeAuthenticationFilter.class.isAssignableFrom(event.getGeneratedBy())){
            //Remember Me的触发
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpServletResponse response = servletRequestAttributes.getResponse();

            //这里需要补充触发session过期逻辑，避免相同用户并发登录
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if( authentication != null ){
                getSessionStrategy().onAuthentication(authentication,request,response);
            }

            this.writeTenantAfterAuthSuccessHandler.onHandle(request,response);
        }
    }
}
