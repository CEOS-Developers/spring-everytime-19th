package com.ceos19.everytime.domain.AboutCourse;


import com.ceos19.everytime.constant.DayOfWeek;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Course extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    //과목코드
    @Column(nullable = false)
    private String courseCode;

    //분반
    private Integer division;

    //과목명
    @Column(nullable = false)
    private String className;

    //교수명
    private String professor;

    //강의 요일
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek classDay;

    //강의실
    private String location;

    //교시
    private Integer period;

    //학점
    @Column(nullable = false)
    private Integer credit;

    //시작 시간
    private Time startTime;

    //종료 시간
    private Time endTime;

    //강의 설명
    private String description;

    @Builder
    public Course(Long courseId, String courseCode, Integer division, String className, String professor, DayOfWeek classDay, String location, Integer period, Integer credit, Time startTime, Time endTime, String description) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.division = division;
        this.className = className;
        this.professor = professor;
        this.classDay = classDay;
        this.location = location;
        this.period = period;
        this.credit = credit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }
}
