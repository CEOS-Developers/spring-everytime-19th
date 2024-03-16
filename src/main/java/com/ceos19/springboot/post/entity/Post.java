package com.ceos19.springboot.post.entity;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.common.BaseEntity;
import com.ceos19.springboot.postlike.entity.Postlike;
import com.ceos19.springboot.user.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="post")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;

    private String content;

    private Boolean anonymous;

    private Integer view;

    private Integer likes;

    @OneToMany(mappedBy = "post")
    private List<Postlike> postlikeList = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();
}
