package com.fishedee.security_boost.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my")
public class MyController {
    //测试csrf能否关闭，SpringSecurity默认只检查post请求
    @PostMapping("/check")
    public String check(){
        return "123";
    }
}
