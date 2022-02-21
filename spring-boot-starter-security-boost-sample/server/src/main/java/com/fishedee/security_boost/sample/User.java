package com.fishedee.security_boost.sample;

import lombok.Data;

@Data
public class User {

    public enum IsEnabled{
        ENABLE,
        DISABLE,
    }
    private Long id;

    private String name;

    private String password;

    private String roles;

    private String remark;

    private IsEnabled isEnabled;
}
