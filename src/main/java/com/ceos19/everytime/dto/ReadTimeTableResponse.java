package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Semester;
import com.ceos19.everytime.domain.TimeTable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadTimeTableResponse {
    private Long id;
    private String name;
    private int year;
    private Semester semester;

    public static ReadTimeTableResponse from(TimeTable timeTable) {
        return new ReadTimeTableResponse(
                timeTable.getId(),
                timeTable.getName(),
                timeTable.getYear(),
                timeTable.getSemester()
        );
    }
}
