package com.ceos19.springboot.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CommentReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_reply_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, length = 10)
    private Long likeCount;

    @Column(nullable = false, length = 10)
    private Long reportCount;

    @Column(nullable = false, length = 20)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 20)
    private LocalDateTime editedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
