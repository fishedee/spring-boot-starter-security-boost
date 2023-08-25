package com.fishedee.security_boost.sample.multi_tenant;

import com.fishedee.security_boost.SecurityTenantResolver;

public class MySecurityTenantResolver implements SecurityTenantResolver {
    @Override
    public String getTenantId(){
        return MyTenantHolder.getTenantId();
    }
}
