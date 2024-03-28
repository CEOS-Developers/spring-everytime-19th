package com.ceos19.everyTime.error;

import lombok.Getter;

@Getter
public class ErrorResponse<T> {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse error(String message) {
        return new ErrorResponse(message);
    }
}
