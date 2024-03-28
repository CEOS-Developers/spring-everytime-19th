package com.ceos19.springboot.tablecourse.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TableCourseResponseDto {
    private Long courseLectureId;
    private Float courseAverage;
    private Long timeTableId;

    public TableCourseResponseDto(Long courseLectureId, Float courseAverage, Long timeTableId) {

        this.courseLectureId = courseLectureId;
        this.courseAverage = courseAverage;
        this.timeTableId = timeTableId;
    }
}
