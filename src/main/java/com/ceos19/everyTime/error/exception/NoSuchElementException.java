package com.ceos19.everyTime.error.exception;

import com.ceos19.everyTime.error.ErrorCode;
import lombok.Getter;


@Getter
public class NoSuchElementException extends RuntimeException{
    public NoSuchElementException(ErrorCode code) {
        super(code.getMessage());
    }
}
