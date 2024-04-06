package com.ceos19.springeverytime.user.exception;

import com.ceos19.springeverytime.global.exception.ResponseException;

public class UserException extends ResponseException {

    public UserException(UserErrorCode userErrorCode) {
        super(userErrorCode.getMessage(), userErrorCode.getHttpStatus());
    }
}
