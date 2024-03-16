package com.ceos19.springboot.post.entity;

import com.ceos19.springboot.board.entity.Board;
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

    @OneToMany(mappedBy ="post",fetch = FetchType.LAZY)
    private List<Comment>commentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy ="post",fetch = FetchType.LAZY)
    private List<Postlike>postLikeList = new ArrayList<>();
}

