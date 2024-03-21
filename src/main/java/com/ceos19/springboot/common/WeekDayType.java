package com.ceos19.springboot.common;

import lombok.Getter;

@Getter
public enum WeekDayType {
    Monday("월요일"),
    Tuesday("화요일"),
    Wednesday("수요일"),
    Thursday("목요일"),
    Friday("금요일"),
    Saturday("토요일"),
    Sunday("일요일");
    private final String message;

    WeekDayType(String message) {
        this.message = message;
    }

    }
