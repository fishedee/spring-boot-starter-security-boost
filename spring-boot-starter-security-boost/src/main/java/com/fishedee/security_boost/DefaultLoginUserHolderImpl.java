package com.fishedee.security_boost;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class DefaultLoginUserHolderImpl implements LoginUserHolder {
    private Authentication getCurrentAuthentication(){
        //尝试在SecurityContextHolder拉取
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if( securityContext != null ){
            Authentication token = securityContext.getAuthentication();
            if( token != null ){
                return token;
            }
        }

        //尝试在request拉取
        RequestAttributes requestAttributes =  RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        SecurityContextImpl securityContextImpl = (SecurityContextImpl)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        if( securityContextImpl == null ){
            return null;
        }
        Authentication token = securityContextImpl.getAuthentication();
        return token;
    }
    public LoginUser getCurrentUser(){
            LoginUser loginUser = new LoginUser();
            Authentication token = this.getCurrentAuthentication();
            if( token == null ){
                return null;
            }else if( token instanceof UsernamePasswordAuthenticationToken){
                loginUser.setAuthenticationTokenType(LoginUser.AuthenticationTokenType.USERNAME_PASSWORD);
            }else if( token instanceof RememberMeAuthenticationToken){
                loginUser.setAuthenticationTokenType(LoginUser.AuthenticationTokenType.REMEMBER_ME);
            }else if( token instanceof AnonymousAuthenticationToken) {
                //未登录用户
                return null;
            }else {
                throw new RuntimeException("unknown token type "+token.toString());
            }
            loginUser.setUserDetails((UserDetails) token.getPrincipal());
            return loginUser;
    }
}
