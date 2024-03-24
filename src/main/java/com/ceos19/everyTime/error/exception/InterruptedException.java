package com.ceos19.everyTime.error.exception;

import com.ceos19.everyTime.error.ErrorCode;
import lombok.Getter;


@Getter
public class InterruptedException extends RuntimeException {

    public InterruptedException(ErrorCode code) {
        super(code.getMessage());
    }
}
