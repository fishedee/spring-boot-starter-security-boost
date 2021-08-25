package com.fishedee.security_boost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityBoostResponse {
    private int code;

    private String msg;

    private Object data;
}
