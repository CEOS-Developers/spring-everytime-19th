package com.ceos19.springeverytime.postcategory.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PostCategoryErrorCode {
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND,"카테고리가 존재하지 않습니다."),
    CATEGORY_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    PostCategoryErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
