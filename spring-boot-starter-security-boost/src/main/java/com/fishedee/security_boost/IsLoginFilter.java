package com.fishedee.security_boost;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fishedee.security_boost.autoconfig.SecurityBoostProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class IsLoginFilter extends HttpFilter {
    @Autowired
    private LoginUserHolder loginUserHolder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityBoostProperties securityBoostProperties;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = request.getRequestURI();
        if( securityBoostProperties.getIsLoginUrl().equals(url) == false){
            chain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        UserDetails userDetails = null;
        LoginUser loginUser = loginUserHolder.getCurrentUser();
        if( loginUser != null){
            userDetails = loginUser.getUserDetails();
        }
        String result = objectMapper.writeValueAsString(new SecurityBoostResponse(0,"",userDetails));
        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
    }
}