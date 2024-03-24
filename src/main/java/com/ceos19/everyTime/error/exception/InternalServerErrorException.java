package com.ceos19.everyTime.error.exception;

import com.ceos19.everyTime.error.ErrorCode;
import lombok.Getter;


@Getter
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(ErrorCode code) {
        super(code.getMessage());
    }
}
