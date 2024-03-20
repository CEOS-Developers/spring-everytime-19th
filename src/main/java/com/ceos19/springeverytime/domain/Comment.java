package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Comment {
    @Id@GeneratedValue
    private Long commentId;

    @NonNull
    @Column(length = 2000, nullable = false)
    private String content;

    @NonNull
    @Column(nullable = false)
    private boolean isAnonymous;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
