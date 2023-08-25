package com.fishedee.security_boost;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RememberMeSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Autowired
    private WriteTenantAfterAuthSuccessHandler writeTenantAfterAuthSuccessHandler;

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event){
        if( RememberMeAuthenticationFilter.class.isAssignableFrom(event.getGeneratedBy())){
            //Remember Me的触发
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpServletResponse response = servletRequestAttributes.getResponse();
            this.writeTenantAfterAuthSuccessHandler.onHandle(request,response);
        }
    }
}
