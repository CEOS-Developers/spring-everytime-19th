package com.ceos19.everyTime.error.exception;

import com.ceos19.everyTime.error.ErrorCode;
import lombok.Getter;


@Getter
public class BadRequestException extends RuntimeException{
    public BadRequestException(ErrorCode code) {
        super(code.getMessage());
    }
}
