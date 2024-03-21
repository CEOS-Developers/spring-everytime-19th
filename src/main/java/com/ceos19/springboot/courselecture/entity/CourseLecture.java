package com.ceos19.springboot.courselecture.entity;

import com.ceos19.springboot.lecture.entity.Lecture;
import com.ceos19.springboot.tablecourse.entity.TableCourse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "course_lecture")
public class CourseLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseLectureId;

    private Float lectureGrade;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_course_id")
    private TableCourse tableCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
}
