package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.School;
import lombok.Data;

@Data
public class FindSchoolResponse {
    private Long id;
    private String name;

    public static FindSchoolResponse from(School school) {
        FindSchoolResponse findSchoolResponse = new FindSchoolResponse();
        findSchoolResponse.id = school.getId();
        findSchoolResponse.name = school.getName();
        return findSchoolResponse;
    }
}
