package com.fishedee.security_boost;

import com.fishedee.security_boost.autoconfig.SecurityBoostProperties;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
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
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        log.info("{}", securityBoostProperties.isCsrfEnable());
        if( securityBoostProperties.isCsrfEnable()){
            http.csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }else{
            http.csrf().disable();
        }

        if( securityBoostProperties.isRememberMeEnabled() ){
            //记住我,必须用check-box传入一个remeber-me的字段
            //使用记住我以后,maximumSessions为1是没有意义的,因为他能被自动登录
            http.rememberMe()
                    .rememberMeParameter(securityBoostProperties.getRememberMeParameter())
                    .userDetailsService(defaultUserDetailService)
                    .tokenRepository(jdbcTokenRepository)
                    .tokenValiditySeconds(securityBoostProperties.getRememberMeSeconds());
        }

        http
                //设置认证异常与授权异常的处理
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                //表单登录的处理
                //必须要用urlEncode的参数来传入
                .formLogin()
                .permitAll()
                .loginProcessingUrl(securityBoostProperties.getLoginUrl())
                .usernameParameter(securityBoostProperties.getLoginUsernameParameter())//登录的用户名字段名称
                .passwordParameter(securityBoostProperties.getLoginPasswordParameter())//登录的密码字段名称
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .and()
                //登出的处理
                .logout()
                .permitAll()
                .logoutUrl(securityBoostProperties.getLogoutUrl())
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                //单个用户的最大可在线的会话数
                .sessionManagement()
                //.invalidSessionStrategy(invalidSessionStrategy)
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy);

        http.addFilterAfter(isLoginFilter, AnonymousAuthenticationFilter.class);
        configureAuthorizeRequests(http);
    }
}
