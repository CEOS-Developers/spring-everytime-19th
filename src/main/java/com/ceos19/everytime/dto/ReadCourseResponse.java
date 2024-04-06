package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ReadCourseResponse {
    private Long id;
    private String name;
    private String courseNumber;
    private String professorName;
    private int credit;
    private int openingGrade;
    private String room;

    public static ReadCourseResponse from(Course course) {
        return new ReadCourseResponse(course.getId(),
                course.getName(),
                course.getCourseNumber(),
                course.getProfessorName(),
                course.getCredit(),
                course.getOpeningGrade(),
                course.getRoom());
    }
}
