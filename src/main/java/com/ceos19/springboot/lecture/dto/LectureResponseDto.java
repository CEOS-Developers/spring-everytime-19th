package com.ceos19.springboot.lecture.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class LectureResponseDto {
    private Long lectureId;

    private LocalTime startTime;

    private LocalTime endTime;

    private String classRoom;

    private String name;

    private String lectureCode;

    private String professor;

    private Integer credit;

    public LectureResponseDto(Long lectureId, LocalTime startTime, LocalTime endTime, String classRoom, String name, String lectureCode, String professor, Integer credit) {
        this.lectureId = lectureId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classRoom = classRoom;
        this.name = name;
        this.lectureCode = lectureCode;
        this.professor = professor;
        this.credit = credit;
    }
}
