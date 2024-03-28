package com.ceos19.everytime.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BaseResponse<T> {
    private HttpStatus httpStatus;
    private String message;
    private T value;
    private int count;

    public BaseResponse(HttpStatus httpStatus, String message, T value, int count) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.value = value;
        this.count = count;
    }
}
