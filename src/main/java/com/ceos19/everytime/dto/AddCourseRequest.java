package com.ceos19.everytime.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCourseRequest {
    @NotBlank
    @Size(max = 20)
    private String courseNumber;

    @NotBlank
    @Size(max = 30)
    private String name;

    private int openingGrade;
    @NotNull
    @Size(max = 50)
    private String professorName;

    private int credit;

    @NotNull
    @Size(max = 50)
    private String room;
}
