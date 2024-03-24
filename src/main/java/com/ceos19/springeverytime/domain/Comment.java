package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
public class Comment {
    @Id@GeneratedValue
    private Long commentId;

    @NonNull
    @Column(length = 2000, nullable = false, updatable = false)
    private String content;

    @NonNull
    @Column(nullable = false, updatable = false)
    private boolean isAnonymous;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", updatable = false)
    @Setter
    private Comment parentComment;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", updatable = false)
    private Post post;

    @Builder
    public Comment(@NonNull String content, @NonNull boolean isAnonymous, @NonNull User author, Comment parentComment, @NonNull Post post) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.author = author;
        this.parentComment = parentComment;
        this.post = post;
    }
}
