package com.ceos19.everytime.domain.AboutCourse;

import com.ceos19.everytime.domain.BaseTimeEntity;
import com.ceos19.everytime.domain.AboutUser.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Timetable extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timetableId;

    @Column(nullable = false)
    private String timetableName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "timetable")
    List<AddedCourse> addedCourses = new ArrayList<AddedCourse>();

    @Builder
    public Timetable(Long timetableId, String timetableName, User user, List<AddedCourse> addedCourses) {
        this.timetableId = timetableId;
        this.timetableName = timetableName;
        this.user = user;
        this.addedCourses = addedCourses;
    }
}
