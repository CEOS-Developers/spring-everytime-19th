package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.School;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadSchoolResponse {
    private Long id;
    private String name;

    public static ReadSchoolResponse from(School school) {
        return new ReadSchoolResponse(school.getId(), school.getName());
    }
}
