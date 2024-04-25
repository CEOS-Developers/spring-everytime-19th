package com.ceos19.everytime.handler;

import com.ceos19.everytime.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)  // @Valid에서 에러가 발생한 경우 여기에서 처리
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(new BaseResponse<>(HttpStatus.BAD_REQUEST, "request parameter error", errors, 0));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)  // @Valid에서 에러가 발생한 경우 여기에서 처리
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(HandlerMethodValidationException ex){
        for (ParameterValidationResult result : ex.getValueResults()) {
            System.out.println("result = " + result);
        }

        return ResponseEntity.badRequest()
                .body(new BaseResponse<>(HttpStatus.BAD_REQUEST, "request parameter error", null, 0));
    }


}
