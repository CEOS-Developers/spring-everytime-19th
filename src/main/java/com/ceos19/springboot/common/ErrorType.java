package com.ceos19.springboot.common;

import lombok.Getter;

@Getter
public class ErrorType {

    private int code;
    private String message;

    // Error Type 생성자 생성
    ErrorType(int code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
