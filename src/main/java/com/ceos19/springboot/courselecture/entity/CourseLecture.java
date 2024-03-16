package com.ceos19.springboot.courselecture.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "course_lecture")
public class CourseLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseLectureId;

    private Float lectureGrade;
}
