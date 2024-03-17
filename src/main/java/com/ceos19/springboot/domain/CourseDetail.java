package com.ceos19.springboot.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "course_detail")
public class CourseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_detail_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String courseCode;

    @Column(nullable = false, length = 20)
    private String day;

    @Column(nullable = false, length = 5)
    private Integer startTime;

    @Column(nullable = false, length = 5)
    private Integer endTime;

    @Column(nullable = false, length = 20)
    private String classroom;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private Course course;
}
