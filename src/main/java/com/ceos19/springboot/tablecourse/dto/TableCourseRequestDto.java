package com.ceos19.springboot.tablecourse.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TableCourseRequestDto {
    private Long courseLectureId;
    private Float courseAverage;
    private Long timeTableId;

    public TableCourseRequestDto(Long courseLectureId, Float courseAverage, Long timeTableId) {

        this.courseLectureId = courseLectureId;
        this.courseAverage = courseAverage;
        this.timeTableId = timeTableId;
    }
}
