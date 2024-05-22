package com.ceos19.everytime.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum SuccessCode {

    SELECT_SUCCESS(OK, "SELECT_SUCCESS"),
    DELETE_SUCCESS(OK, "DELETE_SUCCESS"),
    INSERT_SUCCESS(CREATED, "INSERT_SUCCESS"),
    UPDATE_SUCCESS(NO_CONTENT, "UPDATE SUCCESS"),
    ;

    private final HttpStatus httpStatus;    // 코드 상태
    private final String message;           // 코드 메시지


    SuccessCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
