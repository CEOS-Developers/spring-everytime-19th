package com.ceos19.springboot.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_reply")
public class CommentReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_reply_id", nullable = false)
    private Long id;

    @Column(nullable = true, length = 1000)
    private String content;

    @Column(nullable = false, length = 10)
    private Long like;

    @Column(nullable = false, length = 10)
    private Long report;

    @Column(nullable = true, length = 20)
    private LocalDateTime createdAt;

    @Column(nullable = true, length = 20)
    private LocalDateTime editedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
