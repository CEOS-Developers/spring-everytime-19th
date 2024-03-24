package com.ceos19.everyTime.error;

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
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    // Custom Bad Request Error
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    protected ErrorResponse<String> handleBadRequestException(BadRequestException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ErrorResponse.error(exception.getMessage());
    }



    // Custom Unauthorized Error
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    protected ErrorResponse<String> handleUnauthorizedException(UnauthorizedException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ErrorResponse.error(exception.getMessage());
    }

    // Custom Internal Server Error
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    protected ErrorResponse<String> handleInternalServerErrorException(
        InternalServerErrorException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ErrorResponse.error(exception.getMessage());
    }

    // @RequestBody valid 에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse<String> handleMethodArgNotValidException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request) {
        String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        logInfo(request, message);
        return ErrorResponse.error(exception.getMessage());
    }

    // @ModelAttribute valid 에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    protected ErrorResponse<String> handleMethodArgNotValidException(BindException exception,
        HttpServletRequest request) {
        String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        logInfo(request, message);
        return ErrorResponse.error(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse<String> handleNotFoundException(NotFoundException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ErrorResponse.error(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateException.class)
    public ErrorResponse<String> handleDuplicationException(DuplicateException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ErrorResponse.error(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorResponse<String> handlerForbiddenException(ForbiddenException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ErrorResponse.error(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse<String> handlerNoSuchElementException(ForbiddenException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ErrorResponse.error(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InterruptedException.class)
    public ErrorResponse<String> handlerNoSuchElementException(InterruptedException exception,
        HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ErrorResponse.error(exception.getMessage());
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