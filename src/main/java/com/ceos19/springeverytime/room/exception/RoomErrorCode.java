package com.ceos19.springeverytime.room.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RoomErrorCode {

    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 대화방입니다."),
    ROOM_ALREADY_EXIST(HttpStatus.CONFLICT,"이미 존재하는 대화방입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    RoomErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
