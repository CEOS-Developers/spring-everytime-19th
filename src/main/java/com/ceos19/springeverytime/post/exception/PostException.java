package com.ceos19.springeverytime.post.exception;

import com.ceos19.springeverytime.global.exception.ResponseException;

public class PostException extends ResponseException {

    protected PostException(PostErrorCode postErrorCode) {
        super(postErrorCode.getMessage(), postErrorCode.getHttpStatus());
    }
}
