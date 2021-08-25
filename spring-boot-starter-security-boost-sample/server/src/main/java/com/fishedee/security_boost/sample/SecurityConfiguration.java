package com.fishedee.security_boost.sample;

import com.fishedee.security_boost.SecurityBoostConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfiguration extends SecurityBoostConfiguration {
}
