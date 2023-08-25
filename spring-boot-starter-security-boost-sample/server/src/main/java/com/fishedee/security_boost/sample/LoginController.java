package com.fishedee.security_boost.sample;

import com.fishedee.security_boost.DefaultUserDetail;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class LoginController {
    @GetMapping("/islogin")
    public Result<DefaultUserDetail> islogin(){
        RequestAttributes requestAttributes =  RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        SecurityContextImpl securityContextImpl = (SecurityContextImpl)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        if( securityContextImpl == null ){
            return Result.success(null);
        }else{
            AbstractAuthenticationToken token = (AbstractAuthenticationToken) securityContextImpl.getAuthentication();
            DefaultUserDetail userDetail = (DefaultUserDetail)token.getPrincipal();
            return Result.success(userDetail);
        }
    }
}
