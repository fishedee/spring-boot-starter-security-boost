package com.fishedee.security_boost.sample.multi_tenant;

import com.fishedee.security_boost.SecuritySceneResolver;
import com.fishedee.security_boost.SecurityTenantResolver;

public class MySecuritySceneResolver implements SecuritySceneResolver {
    @Override
    public String getSceneId(){
        return MySecuritySceneHolder.getSceneId();
    }
}

