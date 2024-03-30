package com.ceos19.everytime.domain.AboutUser;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.domain.AboutCourse.Timetable;
import com.ceos19.everytime.domain.AboutPost.Comment;
import com.ceos19.everytime.domain.AboutPost.Post;
import com.ceos19.everytime.domain.AboutPost.Scrap;
import com.ceos19.everytime.domain.BaseTimeEntity;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 15, nullable = false)
    private String loginId;

    @Column(length = 20, nullable = false)
    private String loginPassword;

    @Column(nullable = false)
    private String username;

    @Column(length = 20, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private Timestamp loginAt = Timestamp.valueOf(LocalDateTime.now());

    //User->School
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(mappedBy = "user")
    List<Timetable> timetables = new ArrayList<Timetable>();

    @OneToMany(mappedBy = "user")
    List<Scrap> scraps = new ArrayList<Scrap>();

    @OneToMany(mappedBy = "user")
    List<Post> posts = new ArrayList<Post>();

    @OneToMany(mappedBy = "user")
    List<Comment> comments = new ArrayList<Comment>();

    @Builder
    public User(Long userId, String loginId, String loginPassword, String username, String nickname, String department, Long studentId, String email, boolean isActive, Timestamp loginAt, School school) {
        this.userId = userId;
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.username = username;
        this.nickname = nickname;
        this.department = department;
        this.studentId = studentId;
        this.email = email;
        this.isActive = isActive;
        this.loginAt = loginAt;
        this.school = school;
    }

    public User(String username, School school){
        this.username = username;
        this.school = school;
    }
}
