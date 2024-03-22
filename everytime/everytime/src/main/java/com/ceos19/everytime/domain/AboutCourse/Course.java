package com.ceos19.everytime.domain.AboutCourse;


import com.ceos19.everytime.constant.DayOfWeek;
import com.ceos19.everytime.domain.AboutCourse.AddedCourse;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Course extends BaseTimeEntity {

    @Id
    @Column(name="course_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long courseId;

    //과목코드
    @Column(name="course_code", nullable = false)
    private String courseCode;

    //분반
    private Integer division;

    //과목명
    @Column(name="class_name", nullable = false)
    private String className;

    //교수명
    private String professor;

    //강의 요일
    @Enumerated(EnumType.STRING)
    @Column(name="class_day", nullable = false)
    private DayOfWeek classDay;

    //강의실
    private String location;

    //교시
    private Integer period;

    //학점
    @Column(nullable = false)
    private Integer credit;

    //시작 시간
    @Column(name="start_time")
    private Time startTime;

    //종료 시간
    @Column(name="end_time")
    private Time endTime;

    //강의 설명
    private String description;

    @OneToMany(mappedBy = "course")
    List<AddedCourse> addedCourses = new ArrayList<AddedCourse>();

}
