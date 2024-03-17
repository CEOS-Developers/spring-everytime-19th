package com.ceos19.springboot.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(nullable = true, length = 1000)
    private String content;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false, length = 10)
    private Long like;

    @Column(nullable = false, length = 10)
    private Long report;

    @Column(nullable = false, length = 20)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 20)
    private LocalDateTime editedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;
}
