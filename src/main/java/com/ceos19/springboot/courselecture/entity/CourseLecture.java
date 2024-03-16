package com.ceos19.springboot.courselecture.entity;

import com.ceos19.springboot.lecture.entity.Lecture;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "course_lecture")
public class CourseLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseLectureId;

    private Float lectureGrade;

    @OneToOne(mappedBy = "lecture", cascade = CascadeType.REMOVE)
    private Lecture lecture;
}
