package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;

@Entity
public class Comment {
    @Id
    private Long id;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isAnonymous;

    @ManyToOne
    private User author;

    @ManyToOne
    private Comment parentComment;

    @ManyToOne
    private Post post;
}
