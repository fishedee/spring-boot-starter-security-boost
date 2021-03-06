package com.fishedee.security_boost;

import lombok.extern.slf4j.Slf4j;
import com.fishedee.security_boost.autoconfig.SecurityBoostProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;


@Slf4j
public class SecurityBoostConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService defaultUserDetailService;

    @Autowired
    private HttpAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private MyAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthSuccessHandler authSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Autowired
    private MySessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private MyInvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private HttpLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IsLoginFilter isLoginFilter;

    @Autowired
    private SecurityBoostProperties securityBoostProperties;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(defaultUserDetailService)
                .passwordEncoder(passwordEncoder);

    }

    protected void configureAuthorizeRequests(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers(securityBoostProperties.getLoginUrl()).permitAll()
                .antMatchers(securityBoostProperties.getLogoutUrl()).permitAll()
                .anyRequest().authenticated();
    }

    public SwitchUserFilter switchUserFilter(){
        SwitchUserFilter filter = new SwitchUserFilter();
        filter.setUserDetailsService(defaultUserDetailService);
        filter.setSwitchUserUrl(securityBoostProperties.getSwitchLoginUrl());
        filter.setSuccessHandler(authSuccessHandler);
        filter.setFailureHandler(authFailureHandler);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        if( securityBoostProperties.isCsrfEnable()){
            http.csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }else{
            http.csrf().disable();
        }

        if( securityBoostProperties.isRememberMeEnabled() ){
            //?????????,?????????check-box????????????remeber-me?????????
            //?????????????????????,maximumSessions???1??????????????????,???????????????????????????
            http.rememberMe()
                    .rememberMeParameter(securityBoostProperties.getRememberMeParameter())
                    .userDetailsService(defaultUserDetailService)
                    .tokenRepository(jdbcTokenRepository)
                    .tokenValiditySeconds(securityBoostProperties.getRememberMeSeconds());
        }

        http
                //??????????????????????????????????????????
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                //?????????????????????
                //????????????urlEncode??????????????????
                .formLogin()
                .permitAll()
                .loginProcessingUrl(securityBoostProperties.getLoginUrl())
                .usernameParameter(securityBoostProperties.getLoginUsernameParameter())//??????????????????????????????
                .passwordParameter(securityBoostProperties.getLoginPasswordParameter())//???????????????????????????
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .and()
                //???????????????
                .logout()
                .permitAll()
                .logoutUrl(securityBoostProperties.getLogoutUrl())
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                //??????????????????????????????????????????
                .sessionManagement()
                //.invalidSessionStrategy(invalidSessionStrategy)
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy);

        http.addFilterAfter(isLoginFilter, AnonymousAuthenticationFilter.class);

        if( securityBoostProperties.isSwitchLoginEnable() ){
            http.addFilterAfter(switchUserFilter(), FilterSecurityInterceptor.class);
        }
        configureAuthorizeRequests(http);
    }
}
