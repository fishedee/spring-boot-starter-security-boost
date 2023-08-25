package com.fishedee.security_boost.sample.multi_tenant;

import com.fishedee.security_boost.SecurityTenantResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile({"production"})
public class MyTenantConfig  {
    @Bean("firstDataSource")
    @ConfigurationProperties(prefix="spring.datasource.first")
    public DataSource firstDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("secondDataSource")
    @ConfigurationProperties(prefix="spring.datasource.second")
    public DataSource secondDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public MyTenantDataSource dataSource(DataSource firstDataSource, DataSource secondDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put("first", firstDataSource);
        targetDataSources.put("second", secondDataSource);
        //默认返回的也是一个datasource，也可以不填
        //new DynamicDataSource(firstDataSource, targetDataSources);
        return new MyTenantDataSource(null,targetDataSources);
    }

    @Bean
    public FilterRegistrationBean extractTenantFilter(){
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ExtractTenantDataSourceFilter());//设置过滤器名称
        filterRegistrationBean.addUrlPatterns("/*");//配置过滤规则
        filterRegistrationBean.setOrder(-1000); //order的数值越小 则优先级越高，必须要比Spring Security要更提前地设置
        return filterRegistrationBean;
    }

    @Bean
    @Primary
    public SecurityTenantResolver securityTenantResolver(){
        return new MySecurityTenantResolver();
    }

    @Bean
    public CheckTenantFilter checkTenantFilter(){
        return new CheckTenantFilter();
    }

   }
