package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="User")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class User {
    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long user_id;

    @Column(length=15, nullable = false)
    private String login_id;

    @Column(length=20, nullable = false)
    private String login_password;

    @Column(nullable = false)
    private String username;

    @Column(length=20, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Long student_id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean is_active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date latest_login_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date join_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date login_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at;


    //User->School
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="school_id")
    private School school;

    //Timetable -> User
    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<Timetable> timetables = new ArrayList<Timetable>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<SubComment> subcomments = new ArrayList<SubComment>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<Scrap> scraps = new ArrayList<Scrap>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<Post> posts = new ArrayList<Post>();

    @OneToMany(mappedBy = "user1")
    @Builder.Default
    List<Friend> friends1 = new ArrayList<Friend>();

    @OneToMany(mappedBy = "user2")
    @Builder.Default
    List<Friend> friends2 = new ArrayList<Friend>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<Comment> comments = new ArrayList<Comment>();

    @OneToOne(mappedBy = "user")
    private MessageBox messageBox;

    @OneToMany(mappedBy = "user1")
    @Builder.Default
    List<Message> messages1 = new ArrayList<Message>();

    @OneToMany(mappedBy = "user2")
    @Builder.Default
    List<Message> messages2 = new ArrayList<Message>();
}
