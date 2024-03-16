package com.ceos19.springboot.tablecourse.entity;

import com.ceos19.springboot.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "table_course")
public class TableCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableCourseId;

    private Float courseAverage;
}
