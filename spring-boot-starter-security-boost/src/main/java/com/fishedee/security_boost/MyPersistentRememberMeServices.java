package com.fishedee.security_boost;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class MyPersistentRememberMeServices extends PersistentTokenBasedRememberMeServices {

    private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();;

    public MyPersistentRememberMeServices(UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository){
        super(UUID.randomUUID().toString(),userDetailsService,tokenRepository);
    }


    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }

    @Override
    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
        Authentication auth = super.createSuccessfulAuthentication(request,user);


        //FIXME，需要传入response
        //this.sessionStrategy.onAuthentication(auth, request, response);
        return auth;
    }

}
