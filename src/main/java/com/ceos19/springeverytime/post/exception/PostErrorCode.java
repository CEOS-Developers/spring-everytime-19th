package com.ceos19.springeverytime.post.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PostErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"게시물이 존재하지 않습니다"),
    POST_ALREADY_EXIST(HttpStatus.CONFLICT,"이미 존재하는 게시물입니다");

    private final HttpStatus httpStatus;
    private final String message;

    PostErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
