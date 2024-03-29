package com.ceos19.springboot.school.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class School {
    @Id
    @GeneratedValue
    private Long schoolId;

    private String schoolName;

    private String dept;

    public School(String schoolName,
                  String dept) {
        this.schoolName = schoolName;
        this.dept = dept;
    }
}
