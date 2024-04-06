package com.ceos19.springeverytime.global.exception;

import org.springframework.http.HttpStatus;

public abstract class ResponseException extends RuntimeException {
    private final HttpStatus status;

    protected ResponseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
