package com.fishedee.security_boost;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by fish on 2021/4/26.
 */
//认证成功的处理器
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(javax.servlet.http.HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String result = mapper.writeValueAsString(new SecurityBoostResponse(0,"",authentication.getPrincipal()));

        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
    }
}