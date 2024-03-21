package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Timetable")
@Getter
@Setter
public class Timetable {

    @Id
    @Column(name="timetable_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long timetable_id;

    @Column(name="timetable_name")
    private String timetable_name;

    @Column(name="created_at", nullable = false)
    private Timestamp created_at;

    @Column(name="updated_at", nullable = false)
    private Timestamp updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    @OneToMany(mappedBy = "timetable")
    List<AddedCourse> addedCourses = new ArrayList<AddedCourse>();
}
