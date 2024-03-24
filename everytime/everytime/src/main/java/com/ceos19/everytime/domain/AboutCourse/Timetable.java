package com.ceos19.everytime.domain.AboutCourse;

import com.ceos19.everytime.domain.BaseTimeEntity;
import com.ceos19.everytime.domain.AboutUser.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Timetable extends BaseTimeEntity {

    @Id
    @Column(name="timetable_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long timetableId;

    @Column(name="timetable_name", nullable = false)
    private String timetableName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    @OneToMany(mappedBy = "timetable")
    @Builder.Default
    List<AddedCourse> addedCourses = new ArrayList<AddedCourse>();
}
