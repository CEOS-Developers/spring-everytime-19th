package com.ceos19.everytime.global.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public record ErrorResponse(int status, String message) {

    public static ErrorResponse from(final Exception e) {
        return new ErrorResponse(NOT_FOUND.value(), e.getMessage());
    }
}
