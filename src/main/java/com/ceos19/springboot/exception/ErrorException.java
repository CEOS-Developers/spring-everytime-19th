package com.ceos19.springboot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;
}
