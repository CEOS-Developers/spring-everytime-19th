package com.ceos19.springboot.user.entity;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.commentlike.entity.CommentLike;
import com.ceos19.springboot.common.UserRoleEnum;
import com.ceos19.springboot.friend.entity.Friend;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.postlike.entity.Postlike;
import com.ceos19.springboot.reply.entity.Reply;
import com.ceos19.springboot.school.entity.School;
import com.ceos19.springboot.timetable.entity.TimeTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
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


}
