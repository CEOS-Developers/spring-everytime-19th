package com.ceos19.springboot.user.entity;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.commentlike.entity.CommentLike;
import com.ceos19.springboot.common.LoginType;
import com.ceos19.springboot.common.UserRoleEnum;
import com.ceos19.springboot.friend.entity.Friend;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.postlike.entity.Postlike;
import com.ceos19.springboot.reply.entity.Reply;
import com.ceos19.springboot.school.entity.School;
import com.ceos19.springboot.timetable.entity.TimeTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length =20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private String nickName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @Column(nullable = true)
    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Postlike> postLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<CommentLike> commentLikesList = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Post> postList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_table_id")
    private TimeTable timeTable;

    @JsonIgnore
    @OneToMany(mappedBy = "user1",cascade = CascadeType.REMOVE)
    private List<Friend> friend1 = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user2",cascade = CascadeType.REMOVE)
    private List<Friend> friend2 = new ArrayList<>();


    @Builder
    public User(LoginType socialType, String socialId) {
        this.loginType = socialType;
        this.username = socialId;
    }

    public User() {

    }

    public static User of(LoginType loginType,  String username, String password, UserRoleEnum role) {
        User user = new User(loginType, username); // 일반 로그인 타입으로 사용자 생성
        user.setPassword(password); // 패스워드 설정
        user.setRole(role); // 역할 설정
        return user; // 사용자 반환
    }

}
