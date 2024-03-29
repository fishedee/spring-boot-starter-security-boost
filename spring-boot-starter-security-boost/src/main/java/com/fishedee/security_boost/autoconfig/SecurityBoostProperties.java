package com.fishedee.security_boost.autoconfig;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix="spring.security-boost")
public class SecurityBoostProperties {
    private boolean enable;

    private String userTable = "user";

    private String loginUrl = "/login/login";

    private String loginUsernameParameter = "user";

    private String loginPasswordParameter = "password";

    private String logoutUrl = "/login/logout";

    private String isLoginUrl = "/login/islogin";

    private boolean switchLoginEnable = true;

    private String switchLoginUrl = "/login/switch";

    private boolean csrfEnable = true;

    private boolean rememberMeEnabled = true;

    private String rememberMeParameter = "remember-me";

    private int rememberMeSeconds = 60*60*24*7;//7天

    private String tenantCookieName = "tenant_id";

    private int tenantCookieAge = 60*60*24*7;//7天

    private List<String> unLimitSessionTenant = new ArrayList<>();//没有同时登录人数限制的租户
}
