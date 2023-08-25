package com.fishedee.security_boost.sample.multi_tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile({"production"})
public class MyTenantWebConfig implements WebMvcConfigurer {
    @Autowired
    private CheckTenantFilter checkTenantFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(checkTenantFilter);
    }

}
