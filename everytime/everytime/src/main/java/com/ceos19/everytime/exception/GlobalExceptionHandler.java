package com.ceos19.everytime.exception;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * '@Validated'으로 binding error 발생시 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     * MethodArgumentNotValidException: request body의 데이터가 유효하지 않을 때 발생하는 에러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e); // 로그 기록 추가
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.REQUEST_BODY_MISSING_ERROR);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    /**
     * JPA를 통해 DB 조작시 발생
     * ConstraintViolationException : 제약 조건 위배되었을 때 발생
     * DataIntegrityViolationException : 데이터의 삽입/수정이 무결성 제약 조건을 위반할 때 발생
     */
    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ErrorResponse> handleDataException(Exception e) {
        log.error("DataException: ", e); // 로그 기록 추가
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.DUPLICATE_RESOURCE);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }


    /**
     * HttpRequestMethodNotSupportedException: 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException: ", e); // 로그 기록 추가
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    /**
     * CustomException: Business Logic 수행 중 발생시킬 커스텀 에러
     * 여기서 CustomException은 'PostNotFoundException'으로 게시글이 발견되지 않을 때 발생하는 에러를 처리한다
     */
    @ExceptionHandler(value = { PostNotFoundException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(PostNotFoundException e) {
        log.error("PostNotFoundException: ", e); // 로그 기록 추가
        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND_ERROR); // CustomException에 ErrorCode Enum 반환
        return ResponseEntity.status(response.getStatus()).body(response);
    }



    /**
     * 위에 해당하는 예외에 해당하지 않을 때 모든 예외를 처리하는 메소드
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled exception:", e); // 예외 정보를 로그로 기록
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

}
