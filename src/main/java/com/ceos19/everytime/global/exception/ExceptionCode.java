package com.ceos19.everytime.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 회원은 존재하지 않습니다."),
    NOT_FOUND_SCHOOL(HttpStatus.NOT_FOUND, "해당 학교는 존재하지 않습니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "해당 게시글은 존재하지 않습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 댓글은 존재하지 않습니다."),
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "해당 게시판은 존재하지 않습니다."),
    NOT_FOUND_POST_LIKE(HttpStatus.NOT_FOUND, "해당 게시글 좋아요는 존재하지 않습니다."),
    NOT_FOUND_COMMENT_LIKE(HttpStatus.NOT_FOUND, "해당 댓글 좋아요는 존재하지 않습니다."),

    ALREADY_EXIST_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    ALREADY_EXIST_COMMENT_LIKE(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 댓글입니다."),
    ALREADY_EXIST_POST_LIKE(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 게시글입니다."),
    INVALID_LIKE_NUMBER(HttpStatus.BAD_REQUEST, "좋아요 수는 0보다 작을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
