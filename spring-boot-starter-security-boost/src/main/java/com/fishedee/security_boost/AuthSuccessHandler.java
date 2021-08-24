package com.fishedee.erp.framework.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fishedee.erp.user.business.User;
import com.fishedee.erp.framework.mvc.MyResponseBodyAdvice;
import com.fishedee.erp.user.infrastructure.UserRepository;
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
@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthSuccessHandler.class);

    @Autowired
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(javax.servlet.http.HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        MyUserDetail userDetail = (MyUserDetail) authentication.getPrincipal();

        String result = mapper.writeValueAsString(new MyResponseBodyAdvice.ResponseResult(0,"",userDetail));

        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
    }
}