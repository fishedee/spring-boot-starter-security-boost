package com.fishedee.security_boost.sample.multi_tenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

public class MyTenantDataSource extends AbstractRoutingDataSource {

    public MyTenantDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        //默认目标数据源
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        //目标数据源集合。数据源切换时从此列表选择
        super.setTargetDataSources(targetDataSources);
        //属性设置
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        //关键：更具数据源key。获取选择的数据源。
        return MyTenantHolder.getDataSourceKey();
    }
}
