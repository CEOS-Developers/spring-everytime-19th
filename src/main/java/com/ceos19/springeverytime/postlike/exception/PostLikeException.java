package com.ceos19.springeverytime.postlike.exception;

import com.ceos19.springeverytime.global.exception.ResponseException;

public class PostLikeException extends ResponseException {

    public PostLikeException(PostLikeErrorCode postLikeErrorCode) {
        super(postLikeErrorCode.getMessage(), postLikeErrorCode.getHttpStatus());
    }
}
