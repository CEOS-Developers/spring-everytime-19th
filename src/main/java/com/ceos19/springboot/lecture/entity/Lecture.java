package com.ceos19.springboot.lecture.entity;

import com.ceos19.springboot.common.BaseEntity;
import com.ceos19.springboot.common.WeekDayType;
import com.ceos19.springboot.courselecture.entity.CourseLecture;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "lecture")
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    private LocalTime startTime;

    private LocalTime endTime;

    private String classRoom;

    private String name;

    private String lectureCode;

    private String professor;

    private Integer credit;

    private List<WeekDayType> lectureDays;

    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY)
    private List<CourseLecture> courseLectureList;
}
