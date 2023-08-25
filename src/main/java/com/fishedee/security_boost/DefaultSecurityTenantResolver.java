package com.fishedee.security_boost;

public class DefaultSecurityTenantResolver implements SecurityTenantResolver{
    @Override
    public String getTenantId(){
        //默认的租户ID为空
        return null;
    }
}
