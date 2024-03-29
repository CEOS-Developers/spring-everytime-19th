package com.ceos19.springeverytime.domain.comment.domain;

import com.ceos19.springeverytime.domain.BaseEntity;
import com.ceos19.springeverytime.domain.like.domain.CommentLike;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@SQLDelete(sql = "UPDATE comment SET isDelete = true WHERE commentId = ?")
@Where(clause = "is_delete = false") // Hibernate 6.3 이후부터는 DEPRECATED 됨. (현재 프로젝트는 6.2.2 적용으로 Where 사용)
public class Comment extends BaseEntity {
    @Id @GeneratedValue
    private Long commentId;

    @NonNull
    @Column(length = 2000, nullable = false, updatable = false)
    private String content;

    @NonNull
    @Column(nullable = false, updatable = false)
    private boolean isAnonymous;

    @Column(nullable = false)
    private boolean isDelete = false;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
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
        this.isDelete = false;
    }

    public CommentLike like(User user) {
        return new CommentLike(user, this);
    }
}
