package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class TimeTableCourse {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "time_table_course_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "time_table_id")
    private TimeTable timeTable;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    public TimeTableCourse(TimeTable timeTable, Course course) {
        this.timeTable = timeTable;
        this.course = course;
    }
}
