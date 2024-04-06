package com.ceos19.springeverytime.postcategory.exception;

import com.ceos19.springeverytime.global.exception.ResponseException;

public class PostCategoryException extends ResponseException {
    public PostCategoryException(PostCategoryErrorCode postCategoryErrorCode) {
        super(postCategoryErrorCode.getMessage(), postCategoryErrorCode.getHttpStatus());
    }
}
