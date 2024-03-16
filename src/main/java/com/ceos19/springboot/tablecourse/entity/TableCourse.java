package com.ceos19.springboot.tablecourse.entity;

import com.ceos19.springboot.common.BaseEntity;
import com.ceos19.springboot.courselecture.entity.CourseLecture;
import com.ceos19.springboot.timetable.entity.TimeTable;
import jakarta.persistence.*;

@Entity
@Table(name = "table_course")
public class TableCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableCourseId;

    private Float courseAverage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTable timeTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courelecture_id")
    private CourseLecture courseLecture;
}
