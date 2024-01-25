package com.fishedee.security_boost.sample.multi_tenant;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ExtractTenantDataSourceFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        //设置登录场景信息
        String sceneId = MySecuritySceneHolder.getSceneIdByRequest(httpRequest);

        //设置租户信息
        String tenantId = MyTenantHolder.getTenantIdByRequest(httpRequest);
        log.info("ExtractTenantDataSourceFilter filter, uri: {}",httpRequest.getRequestURI());
        if(Strings.isBlank(tenantId)){
            if( httpRequest.getRequestURI().contains("/login/islogin") ||
                httpRequest.getRequestURI().contains("/login/logout")){
                //特殊的islogin逻辑，无租户ID时也要返回null
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("application/json");
                String data = "{\"code\":0,\"msg\":\"\",\"data\":null}";
                httpResponse.getWriter().write(data);
                httpResponse.getWriter().flush();
                return;
            }
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json");
            String data = "{\"code\":1,\"msg\":\"租户ID过期，请重新登录\"}";
            httpResponse.getWriter().write(data);
            httpResponse.getWriter().flush();
            return;
        }
        try{
            MySecuritySceneHolder.setSceneId(sceneId);
            MyTenantHolder.setTenantId(tenantId);
            chain.doFilter(request,response);
        }finally {
            MyTenantHolder.clearTenant();
            MySecuritySceneHolder.clearScene();
        }
    }
}
