package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 300, nullable = false)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @OneToOne
    private User manager;
}
