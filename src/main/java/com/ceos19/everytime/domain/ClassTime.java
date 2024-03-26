package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
@ToString(exclude = {"course"})
public class ClassTime {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "class_time_id")
    private Long id;

    @Enumerated(STRING)
    private Weekend dayOfWeek;
    private int timePeriod;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder
    protected ClassTime(Weekend dayOfWeek, int timePeriod, Course course) {
        this.dayOfWeek = dayOfWeek;
        this.timePeriod = timePeriod;
        this.course = course;
    }
}
