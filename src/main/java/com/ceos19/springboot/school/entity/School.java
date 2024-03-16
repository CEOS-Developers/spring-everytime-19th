package com.ceos19.springboot.school.entity;

import com.ceos19.springboot.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "school")
public class School extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolId;

    private String schoolName;
}
