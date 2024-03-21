package com.ceos19.springboot.common;

import lombok.Getter;

@Getter
public enum ErrorType {
    NOT_FOUND(400, "해당 데이터가 존재하지 않습니다");
    private int code;
    private String message;

    // Error Type 생성자 생성
    ErrorType(int code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
