package com.ceos19.springeverytime.global.exception;

import com.ceos19.springeverytime.user.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({UserException.class})
    public ResponseEntity<ResponseTemplate> handleCustomException(ResponseException exception){
        return ResponseTemplate.toResponseEntity(exception.getStatus(),exception.getMessage());
    }
}
