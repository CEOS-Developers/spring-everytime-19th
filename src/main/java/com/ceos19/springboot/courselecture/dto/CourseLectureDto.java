package com.ceos19.springboot.courselecture.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CourseLectureDto {

    private Long courseLectureId;

    private Float lectureGrade;
    private String table_course_name;
    private String lecture_name;

    public CourseLectureDto(Long courseLectureId, Float lectureGrade, String table_course_name, String lecture_name) {
        this.courseLectureId = courseLectureId;
        this.lectureGrade = lectureGrade;
        this.table_course_name = table_course_name;
        this.lecture_name = lecture_name;
    }
}
