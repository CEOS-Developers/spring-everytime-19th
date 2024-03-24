package com.ceos19.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 20)
    private String professor;

    @Column(nullable = false, length = 5)
    private Integer credit;

    @Column(nullable = false, length = 5)
    private Integer hours;
}
