package com.ceos19.everytime.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DATA_ALREADY_EXISTED(CONFLICT, ""),
    NO_DATA_EXISTED(NOT_FOUND, ""),
    NOT_NULL(NO_CONTENT,""),

    ID_DUPLICATED(CONFLICT, ""),
    INVALID_PASSWORD(UNAUTHORIZED, ""),

    NO_DATA_ALLOCATED(FAILED_DEPENDENCY, ""),
    INVALID_REQUEST_DATA(BAD_REQUEST,""),

    KEYWORD_TOO_SHORT(BAD_REQUEST, ""),
    INVALID_URI_ACCESS(NOT_FOUND,"");


    private final HttpStatus httpStatus;
    private final String message;
}
