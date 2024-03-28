package com.ceos19.everyTime.error.exception;

import com.ceos19.everyTime.error.ErrorCode;
import lombok.Getter;


@Getter
public class DuplicateException extends RuntimeException{

    public DuplicateException(ErrorCode code) {
        super(code.getMessage());
    }
}
