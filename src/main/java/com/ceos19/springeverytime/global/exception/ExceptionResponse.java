package com.ceos19.springeverytime.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExceptionResponse {
    private final int code;
    private final String message;
}
