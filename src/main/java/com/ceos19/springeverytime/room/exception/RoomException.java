package com.ceos19.springeverytime.room.exception;

import com.ceos19.springeverytime.global.exception.ResponseException;

public class RoomException extends ResponseException {

    public RoomException(RoomErrorCode roomErrorCode) {
        super(roomErrorCode.getMessage(), roomErrorCode.getHttpStatus());
    }
}
