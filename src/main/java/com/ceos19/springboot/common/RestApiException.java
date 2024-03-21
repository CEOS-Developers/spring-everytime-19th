package com.ceos19.springboot.common;

import lombok.Getter;

import javax.management.RuntimeErrorException;

@Getter
public class RestApiException extends RuntimeException {
    private final ErrorType errorType;
    public RestApiException(ErrorType errorType)
    {
        this.errorType = errorType;
    }
}
