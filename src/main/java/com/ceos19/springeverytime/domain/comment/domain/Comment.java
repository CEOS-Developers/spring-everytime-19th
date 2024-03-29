package com.ceos19.springeverytime.domain.comment.domain;

import com.ceos19.springeverytime.domain.BaseEntity;
import com.ceos19.springeverytime.domain.like.domain.CommentLike;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
public class Comment extends BaseEntity {
    @Id@GeneratedValue
    private Long commentId;

    @NonNull
    @Column(length = 2000, nullable = false, updatable = false)
    private String content;

    @NonNull
    @Setter
    @Column(nullable = false, updatable = false)
    private boolean isAnonymous;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User author;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", updatable = false)
    private Post post;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.PERSIST)
    private List<Comment> childComments = new ArrayList<>();

    @Builder
    public Comment(@NonNull String content, @NonNull boolean isAnonymous, @NonNull User author, Comment parentComment, @NonNull Post post) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.author = author;
        this.parentComment = parentComment;
        this.post = post;
    }

    public Comment addReply(User author, String content, boolean isAnonymous) {
        if (this.parentComment != null) {
            throw new IllegalArgumentException("대댓글에 또 대댓글을 달 수 없습니다.");
        }
        Comment comment = Comment.builder()
                .post(this.post)
                .author(author)
                .content(content)
                .isAnonymous(isAnonymous)
                .parentComment(this).build();
        this.childComments.add(comment);
        return comment;
    }

    public CommentLike like(User user) {
        return new CommentLike(user, this);
    }
}
