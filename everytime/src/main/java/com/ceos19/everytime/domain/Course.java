package com.ceos19.everytime.domain;


import com.ceos19.everytime.constant.DayOfWeek;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Course")
@Getter
@Setter
public class Course {

    @Id
    @Column(name="course_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long course_id;

    //과목코드
    @Column(name="course_code", nullable = false)
    private String course_code;

    //분반
    @Column(name="division")
    private Integer division;

    //과목명
    @Column(name="class_name", nullable = false)
    private String class_name;

    //교수명
    @Column(name="professor")
    private String professor;

    //강의 요일
    @Enumerated(EnumType.STRING)
    @Column(name="class_day", nullable = false)
    private DayOfWeek class_day;

    //강의실
    @Column(name="location")
    private String location;

    //교시
    @Column(name="period")
    private Integer period;

    //학점
    @Column(name="credit")
    private Integer credit;

    //시작 시간
    @Column(name="start_time")
    private Time start_time;

    //종료 시간
    @Column(name="end_time")
    private Time end_time;

    //강의 설명
    @Column(name="description")
    private String description;

    @Column(name="created_at", nullable = false)
    private Timestamp created_at;

    @Column(name="updated_at", nullable = false)
    private Timestamp updated_at;

    @OneToMany(mappedBy = "course")
    List<AddedCourse> addedCourses = new ArrayList<AddedCourse>();

}
