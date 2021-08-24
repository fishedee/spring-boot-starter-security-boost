package com.fishedee.erp.framework.auth;

import com.fishedee.erp.framework.auth.*;
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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

/**
 * Created by fish on 2021/4/26.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService myUserDetailService;

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

    //重置密码编码器
    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder(12);
        return encoder;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return myUserDetailService;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService)
                .passwordEncoder(passwordEncoder());

    }

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                //设置认证异常与授权异常的处理
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                //表单登录的处理
                //必须要用urlEncode的参数来传入
                .formLogin()
                .permitAll()
                .loginProcessingUrl("/login/login")
                .usernameParameter("user")//登录的用户名字段名称
                .passwordParameter("password")//登录的密码字段名称
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .and()
                //记住我,必须用check-box传入一个remeber-me的字段
                //使用记住我以后,maximumSessions为1是没有意义的,因为他能被自动登录
                .rememberMe()
                .userDetailsService(myUserDetailService)
                .tokenRepository(jdbcTokenRepository)
                //3天内免登录
                .tokenValiditySeconds(60*60*24*3)
                .and()
                //登出的处理
                .logout()
                .permitAll()
                .logoutUrl("/login/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                //单个用户的最大可在线的会话数
                .sessionManagement()
                //.invalidSessionStrategy(invalidSessionStrategy)
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy);

        http.authorizeRequests()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/font/**").permitAll()
                .antMatchers("/manage/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/login/login").permitAll()
                .antMatchers("/login/islogin").permitAll()
                .anyRequest().authenticated();
    }
}
