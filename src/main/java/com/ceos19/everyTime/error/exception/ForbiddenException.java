package com.ceos19.everyTime.error.exception;

import com.ceos19.everyTime.error.ErrorCode;
import lombok.Getter;


@Getter
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(ErrorCode code) {
        super(code.getMessage());
    }
}
