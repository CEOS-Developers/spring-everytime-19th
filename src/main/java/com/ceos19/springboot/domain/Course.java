package com.ceos19.springboot.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Long id;

    @Column(nullable = true, length = 20)
    private String title;

    @Column(nullable = true, length = 20)
    private String professor;

    @Column(nullable = true, length = 5)
    private Integer credit;

    @Column(nullable = true, length = 5)
    private Integer hours;
}
