package com.fishedee.security_boost.sample;

import lombok.Data;

@Data
public class Page<T> {
    private int count;

    private T data;
}
