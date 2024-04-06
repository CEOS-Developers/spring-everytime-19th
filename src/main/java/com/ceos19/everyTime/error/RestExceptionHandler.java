package com.ceos19.everyTime.error;

import com.ceos19.everyTime.common.ApiBaseResponse;
import com.ceos19.everyTime.error.exception.BadRequestException;
import com.ceos19.everyTime.error.exception.DuplicateException;
import com.ceos19.everyTime.error.exception.ForbiddenException;
import com.ceos19.everyTime.error.exception.InternalServerErrorException;
import com.ceos19.everyTime.error.exception.NoSuchElementException;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.error.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    // Custom Bad Request Error
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ApiBaseResponse<?>> handleBadRequestException(BadRequestException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiBaseResponse.createError(
            exception.getMessage()));
    }



    // Custom Unauthorized Error

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ApiBaseResponse<?>> handleUnauthorizedException(UnauthorizedException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiBaseResponse.createError(
            exception.getMessage()));
    }

    // Custom Internal Server Error

    @ExceptionHandler(InternalServerErrorException.class)
    protected ResponseEntity<ApiBaseResponse<?>> handleInternalServerErrorException(
        InternalServerErrorException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiBaseResponse.createError(
            exception.getMessage()));
    }

    // @RequestBody valid 에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiBaseResponse<?> handleMethodArgNotValidException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldError().getDefaultMessage();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        logInfo(request, message);
        return ApiBaseResponse.createError(builder.toString());
    }

    // @ModelAttribute valid 에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    protected ApiBaseResponse<?> handleMethodArgNotValidException(BindException exception,
        HttpServletRequest request) {
        String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        logInfo(request, message);
        return ApiBaseResponse.createError(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiBaseResponse<?> handleNotFoundException(NotFoundException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ApiBaseResponse.createError(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateException.class)
    public ApiBaseResponse<?> handleDuplicationException(DuplicateException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ApiBaseResponse.createError(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ApiBaseResponse<?> handlerForbiddenException(ForbiddenException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ApiBaseResponse.createError(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ApiBaseResponse<?> handlerNoSuchElementException(ForbiddenException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ApiBaseResponse.createError(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InterruptedException.class)
    public ApiBaseResponse<?> handlerNoSuchElementException(InterruptedException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ApiBaseResponse.createError(exception.getMessage());
    }

    private void logInfo(HttpServletRequest request, String message) {
        log.info("{} {} : {} (traceId: {})",
            request.getMethod(), request.getRequestURI(), message, getTraceId());
    }

    private void logWarn(HttpServletRequest request, Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        exception.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        log.warn("{} {} (traceId: {})\n{}", request.getMethod(), request.getRequestURI(),
            getTraceId(), stackTrace);
    }

    private String getTraceId() {
        return MDC.get("traceId");
    }
}