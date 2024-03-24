package com.ceos19.everytime.domain.AboutUser;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.domain.AboutCourse.Timetable;
import com.ceos19.everytime.domain.AboutMessage.Message;
import com.ceos19.everytime.domain.AboutMessage.MessageBox;
import com.ceos19.everytime.domain.AboutPost.Comment;
import com.ceos19.everytime.domain.AboutPost.Post;
import com.ceos19.everytime.domain.AboutPost.Scrap;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class User extends BaseTimeEntity {
    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;

    @Column(length=15, nullable = false)
    private String loginId;

    @Column(length=20, nullable = false)
    private String loginPassword;

    @Column(nullable = false)
    private String username;

    @Column(length=20, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive=true;

    @Column(name="login_at",nullable = false)
    @Builder.Default
    private Timestamp loginAt=Timestamp.valueOf(LocalDateTime.now());

    //User->School
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="school_id")
    private School school;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<Timetable> timetables = new ArrayList<Timetable>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<Scrap> scraps = new ArrayList<Scrap>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<Post> posts = new ArrayList<Post>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<Comment> comments = new ArrayList<Comment>();

    public User(String username, School school){
        this.username = username;
        this.school = school;
    }
}
