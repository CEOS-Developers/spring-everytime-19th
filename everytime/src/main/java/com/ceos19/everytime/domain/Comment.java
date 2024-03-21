package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="Comment")
@Getter
@Setter
public class Comment {

    @Id
    @Column(name="comment_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long comment_id;

    @Column(name="contents")
    private String contents;

    @Column(name="like_num", nullable = false)
    private Integer like_num;

    @Column(name="is_deleted", nullable = false)
    private boolean is_deleted;

    @Column(name="is_reported", nullable = false)
    private boolean is_reported;

    @Column(name="created_at", nullable = false)
    private Timestamp created_at;

    @Column(name="updated_at", nullable = false)
    private Timestamp updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;
}
