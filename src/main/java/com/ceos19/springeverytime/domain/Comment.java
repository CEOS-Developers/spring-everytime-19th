package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;

@Entity
public class Comment {
    @Id@GeneratedValue
    private Long commentId;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isAnonymous;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
