package com.ceos19.springboot.timetable.entity;

import com.ceos19.springboot.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "time_table")
public class TimeTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeTableId;

    private String name;

    private Float totalAverage;


}
