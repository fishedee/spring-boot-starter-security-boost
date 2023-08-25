package com.fishedee.security_boost.sample;

import com.fishedee.security_boost.SecurityBoostConfiguration;
import com.fishedee.security_boost.autoconfig.SecurityBoostProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfiguration extends SecurityBoostConfiguration {
    @Autowired
    private SecurityBoostProperties securityBoostProperties;

    @Override
    protected void configureAuthorizeRequests(HttpSecurity http) throws Exception{
        http.csrf()
                .ignoringAntMatchers("/login/islogin")
                .and()
                .authorizeRequests()
                .antMatchers("/login/islogin").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers(securityBoostProperties.getLoginUrl()).permitAll()
                .antMatchers(securityBoostProperties.getLogoutUrl()).permitAll()
                .antMatchers("/my/*").permitAll()
                .anyRequest().authenticated();
    }
}
