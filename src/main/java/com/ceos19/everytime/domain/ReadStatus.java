package com.ceos19.everytime.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReadStatus {
    READ("메시지 읽음"),
    NOT_READ("메시지 안 읽음")
    ;

    private String description;
}
