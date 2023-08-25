package com.fishedee.security_boost;

import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface WriteTenantAfterAuthSuccessHandler {
    public void onHandle(HttpServletRequest request, HttpServletResponse response) ;
}
