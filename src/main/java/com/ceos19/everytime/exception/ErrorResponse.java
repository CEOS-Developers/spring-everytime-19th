package com.ceos19.everytime.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    //private int status;
    private final HttpStatus httpStatus;
    private final String message;

    @Builder
    protected ErrorResponse(final ErrorCode code) {
        this.httpStatus = code.getHttpStatus();
        this.message = code.getMessage();
    }

    protected ErrorResponse(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
