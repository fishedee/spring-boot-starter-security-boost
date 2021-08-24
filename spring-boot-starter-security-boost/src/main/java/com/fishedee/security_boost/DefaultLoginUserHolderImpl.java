package com.fishedee.security_boost;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class DefaultLoginUserHolderImpl implements LoginUserHolder {
    public LoginUser getCurrentUser(){
        RequestAttributes requestAttributes =  RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        SecurityContextImpl securityContextImpl = (SecurityContextImpl)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        if( securityContextImpl == null ){
            return null;
        }else{
            LoginUser loginUser = new LoginUser();
            AbstractAuthenticationToken token = (AbstractAuthenticationToken) securityContextImpl.getAuthentication();
            if( token instanceof UsernamePasswordAuthenticationToken){
                loginUser.setAuthenticationTokenType(LoginUser.AuthenticationTokenType.USERNAME_PASSWORD);
            }else if( token instanceof RememberMeAuthenticationToken){
                loginUser.setAuthenticationTokenType(LoginUser.AuthenticationTokenType.REMEMBER_ME);
            }else{
                throw new RuntimeException("unknown token type "+token.toString());
            }
            loginUser.setUserDetails((UserDetails) token.getPrincipal());
            return loginUser;
        }
    }
}
