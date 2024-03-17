package com.ceos19.springboot.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "school")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;
}
