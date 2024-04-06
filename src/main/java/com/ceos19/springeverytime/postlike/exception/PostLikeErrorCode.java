package com.ceos19.springeverytime.postlike.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PostLikeErrorCode {
    POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 좋아요입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    PostLikeErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
