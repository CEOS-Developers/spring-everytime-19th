package com.ceos19.everyTime.error.exception;

import com.ceos19.everyTime.error.ErrorCode;
import lombok.Getter;


@Getter
public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(ErrorCode code) {
        super(code.getMessage());
    }
}
