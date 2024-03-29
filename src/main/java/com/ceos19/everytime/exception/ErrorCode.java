package com.ceos19.everytime.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    INVALID_PARAMETER(BAD_REQUEST, "Invalid parameter included"),
    DATA_DUPLICATED(CONFLICT, "Resources are duplicated"),
    DATA_NOT_FOUND(NOT_FOUND, "Resource not exists"),
    //NO_CONTENT(NO_CONTENT,"Data is null"),
    //INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "Internal server error"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}