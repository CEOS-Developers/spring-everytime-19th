package com.ceos19.springboot.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 65535)
    private String content;

    @Column(nullable = false, length = 10)
    private Long like;

    @Column(nullable = false, length = 10)
    private Long report;

    @Column(nullable = false, length = 10)
    private Long scrap;

    @Column(nullable = false, length = 20)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 20)
    private LocalDateTime editedAt;

    @Column(nullable = false)
    private boolean isAnonymous;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
}
