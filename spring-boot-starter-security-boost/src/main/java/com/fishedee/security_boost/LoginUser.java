package com.fishedee.security_boost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    //UsernamePasswordAuthenticationToken;输入账号密码登录的当前用户
    //RememberMeAuthenticationToken;使用记住我登录的用户
    public enum AuthenticationTokenType{
        USERNAME_PASSWORD,
        REMEMBER_ME;
    }

    private AuthenticationTokenType authenticationTokenType;

    private UserDetails userDetails;
}
