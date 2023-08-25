package com.fishedee.security_boost.autoconfig;

import com.fishedee.security_boost.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@EnableConfigurationProperties(SecurityBoostProperties.class)
public class SecurityBoostAutoConfiguration {
    private final AbstractApplicationContext applicationContext;

    private final SecurityBoostProperties properties;

    public SecurityBoostAutoConfiguration(AbstractApplicationContext applicationContext, SecurityBoostProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }


    //密码编码器默认Bean
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder(12);
        return encoder;
    }

    //默认的用户服务
    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public UserDetailsService userDetailsService(){
        return new DefaultUserDetailService();
    }

    @Bean
    @ConditionalOnMissingBean(SecurityTenantResolver.class)
    public SecurityTenantResolver securityTenantResolver(){
        return new DefaultSecurityTenantResolver();
    }

    @Bean
    @ConditionalOnMissingBean(WriteTenantAfterAuthSuccessHandler.class)
    public WriteTenantAfterAuthSuccessHandler writeTenantHandler(){
        return new DefaultWriteTenantAfterAuthSuccessHandler();
    }

    //默认的认证失败处理
    @Bean
    @ConditionalOnMissingBean(AuthFailureHandler.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public AuthFailureHandler authFailureHandler(){
        return new AuthFailureHandler();
    }

    //默认的认证成功处理
    @Bean
    @ConditionalOnMissingBean(AuthSuccessHandler.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public AuthSuccessHandler authSuccessHandler(){
        return new AuthSuccessHandler();
    }

    //默认的权限不足处理
    @Bean
    @ConditionalOnMissingBean(HttpAuthenticationEntryPoint.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public HttpAuthenticationEntryPoint httpAuthenticationEntryPoint(){
        return new HttpAuthenticationEntryPoint();
    }

    //默认的登出处理
    @Bean
    @ConditionalOnMissingBean(HttpLogoutSuccessHandler.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public HttpLogoutSuccessHandler httpLogoutSuccessHandler(){
        return new HttpLogoutSuccessHandler();
    }

    //默认的accessDeny处理
    @Bean
    @ConditionalOnMissingBean(MyAccessDeniedHandler.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public MyAccessDeniedHandler myAccessDeniedHandler(){
        return new MyAccessDeniedHandler();
    }

    //默认的会话过期处理，暂时这个没用
    @Bean
    @ConditionalOnMissingBean(MyInvalidSessionStrategy.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public MyInvalidSessionStrategy myInvalidSessionStrategy(){
        return new MyInvalidSessionStrategy();
    }

    //默认的会话超越阈值被提出处理
    @Bean
    @ConditionalOnMissingBean(MySessionInformationExpiredStrategy.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public MySessionInformationExpiredStrategy mySessionInformationExpiredStrategy(){
        return new MySessionInformationExpiredStrategy();
    }

    //默认的当前用户查询
    @Bean
    @ConditionalOnMissingBean(LoginUserHolder.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public LoginUserHolder loginUserHolder(){
        return new DefaultLoginUserHolderImpl();
    }

    /* 不能在Filter里面做isLogin检查，因为RememberMeFilter有时候会直接忽略后面所有的Filter
    //默认的loginController
    @Bean
    @ConditionalOnMissingBean(IsLoginFilter.class)
    @ConditionalOnProperty(value = "spring.security-boost.enable", havingValue = "true")
    public IsLoginFilter isLoginFilter(){
        return new IsLoginFilter();
    }
     */
}