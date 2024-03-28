package com.ceos19.everyTime.error.exception;

import com.ceos19.everyTime.error.ErrorCode;
import lombok.Getter;


@Getter
public class NotFoundException extends RuntimeException {
    public NotFoundException(ErrorCode code) {
        super(code.getMessage());
    }
}
