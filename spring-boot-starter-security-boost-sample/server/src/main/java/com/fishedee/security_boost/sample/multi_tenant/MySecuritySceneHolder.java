package com.fishedee.security_boost.sample.multi_tenant;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
public class MySecuritySceneHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();


    public static String getSceneIdByRequest(HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();

        String sceneId = "";
        while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            if( headerName.toUpperCase().equals("X-LOGIN-SCENE")){
                sceneId = request.getHeader(headerName);
                break;
            }
        }
        if(sceneId == null || Strings.isBlank(sceneId)){
            sceneId = "";
        }
        //只能为mobile或者普通，两种模式
        if( sceneId.equals("mobile") == false && sceneId.equals("") == false){
            sceneId = "";
        }
        return sceneId;
    }

    public static void setSceneId(String sceneId) {
        CONTEXT_HOLDER.set(sceneId);
    }

    public static String getSceneId(){
        String result = CONTEXT_HOLDER.get();

        log.info("sceneId {}",result);
        if( result == null){
            return "";
        }
        return result;
    }

    public static void clearScene() {
        CONTEXT_HOLDER.remove();
    }
}
