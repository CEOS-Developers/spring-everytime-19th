package com.ceos19.springboot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DATA_ALREADY_EXIST(CONFLICT, ""),
    NO_DATA_EXIST(NOT_FOUND, ""),
    NOT_NULL(NO_CONTENT, ""),

    ID_DUPLICATED(CONFLICT, ""),
    INVALID_PASSWORD(UNAUTHORIZED, ""),

    NO_DATA_ALLOCATED(FAILED_DEPENDENCY, ""),

    KEYWORD_TOO_SHORT("BAS_REQUEST", ""),
    INVALID_VALUE_ASSIGNMENT(BAD_REQUEST, ""),
    INVALID_URI_ACCESS(NOT_FOUND, "");


    private final HttpStatus httpStatus;
    private  final String  message;
}
