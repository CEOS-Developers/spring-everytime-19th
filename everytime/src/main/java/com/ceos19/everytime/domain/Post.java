package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Post")
@Getter
@Setter
public class Post {

    @Id
    @Column(name="post_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long post_id;

    @Column(name="title")
    private String title;

    @Column(name="contents")
    private String contents;

    @Column(name="like_num")
    private Long like_num;

    @Column(name="is_reported", nullable = false)
    private boolean is_reported;

    @Column(name="comment_num", nullable = false)
    private Long comment_num;

    @Column(name="scrap_num", nullable = false)
    private Long scrap_num;

    @Column(name="post_at",nullable = false)
    private Timestamp post_at;

    @Column(name="updated_at",nullable = false)
    private Timestamp updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;

    @OneToMany(mappedBy = "post")
    List<Scrap> scraps = new ArrayList<Scrap>();

    @OneToMany(mappedBy = "post")
    List<Comment> comments = new ArrayList<Comment>();

    @OneToMany(mappedBy="post")
    List<Image> images = new ArrayList<Image>();
}
