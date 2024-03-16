package com.ceos19.springboot.timetable.entity;

import com.ceos19.springboot.common.BaseEntity;
import com.ceos19.springboot.tablecourse.entity.TableCourse;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "time_table")
public class TimeTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeTableId;

    private String name;

    private Float totalAverage;

    private Integer totalCredit;

    @OneToMany(mappedBy = "tablecourse")
    private List<TableCourse> tableCourseList = new ArrayList<>();
}
