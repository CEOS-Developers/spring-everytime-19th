package com.ceos19.everytime.global.exception;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(HttpStatus status, String message) {

    public static ExceptionResponse from(final NotFoundException e) {
        final ExceptionCode exceptionCode = e.getErrorCode();
        return new ExceptionResponse(exceptionCode.getStatus(), e.getMessage());
    }

    public static ExceptionResponse from(final BadRequestException e) {
        final ExceptionCode exceptionCode = e.getExceptionCode();
        return new ExceptionResponse(exceptionCode.getStatus(), e.getMessage());
    }

    public static ExceptionResponse from(final Exception e) {
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
