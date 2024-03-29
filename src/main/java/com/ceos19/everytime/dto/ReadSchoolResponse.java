package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.School;
import lombok.Data;

@Data
public class ReadSchoolResponse {
    private Long id;
    private String name;

    public static ReadSchoolResponse from(School school) {
        ReadSchoolResponse readSchoolResponse = new ReadSchoolResponse();
        readSchoolResponse.id = school.getId();
        readSchoolResponse.name = school.getName();
        return readSchoolResponse;
    }
}
