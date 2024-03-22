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
    @Setter
    private Comment parentComment;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
