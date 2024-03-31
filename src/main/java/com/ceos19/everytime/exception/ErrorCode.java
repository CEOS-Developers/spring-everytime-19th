package com.ceos19.everytime.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    INVALID_PARAMETER(BAD_REQUEST, "Invalid parameter included"),
    DATA_DUPLICATED(CONFLICT, "Resources are duplicated"),

    MEMBER_NOT_FOUND(NOT_FOUND, "Member doesn't exist"),
    UNIVERSITY_NOT_FOUND(NOT_FOUND, "University doesn't exist"),
    BOARD_NOT_FOUND(NOT_FOUND, "Board doesn't exist"),
    MESSAGE_NOT_FOUND(NOT_FOUND, "Message doesn't exist"),
    POST_NOT_FOUND(NOT_FOUND, "Post doesn't exist"),
    POST_LIKE_NOT_FOUND(NOT_FOUND, "PostLike doesn't exist"),


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