package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@ToString
public class School {  // 유저 학교 정보
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "school_id")
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String department;

    public School(String name, String department) {
        this.name = name;
        this.department = department;
    }
}
