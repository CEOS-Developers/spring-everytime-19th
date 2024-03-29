package com.ceos19.everytime.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)    // ErrorCode 내의 에러
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(new ErrorResponse(ex.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

}
