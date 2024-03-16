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

    @OneToOne(mappedBy = "school")
    private School school;

    @OneToMany(mappedBy = "friend")
    private List<Friend> friendList = new ArrayList<>();
    @OneToMany(mappedBy = "reply")
    private List<Reply> replyList = new ArrayList<>();
    @OneToMany(mappedBy = "comment")
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "post_like")
    private List<Postlike> postLikeList = new ArrayList<>();
    @OneToMany(mappedBy = "comment_like")
    private List<CommentLike> commentLikesList = new ArrayList<>();
    @OneToMany(mappedBy = "post")
    private List<Post> postList = new ArrayList<>();

    @OneToOne(mappedBy = "time_table")
    private TimeTable timeTable;


}
