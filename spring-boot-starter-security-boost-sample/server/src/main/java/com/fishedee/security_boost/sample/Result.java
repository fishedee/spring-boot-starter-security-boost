package com.fishedee.security_boost.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;

    private String msg;

    private T data;

    public static <T> Result<T> success(T data){
        return new Result<>(0,"",data);
    }

    public static Result<Object> fail(int code,String msg){
        return new Result<Object>(code,msg,null);
    }
}
