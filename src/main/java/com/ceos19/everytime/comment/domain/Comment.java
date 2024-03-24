package com.ceos19.everytime.comment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.ceos19.everytime.global.BaseEntity;
import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.user.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update comment set is_deleted = true where comment_id = ?")
@SQLRestriction("is_deleted = false")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private int likeNumber = 0;

    @Column(nullable = false)
    private boolean isAnonymous;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Builder
    public Comment(final String content, final boolean isAnonymous, final User user, final Post post,
                   final Comment parentComment) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.user = user;
        this.post = post;
        this.parentComment = parentComment;
    }

    public static Comment createComment(final String content, final boolean isAnonymous, final User user,
                                        final Post post, final Comment parentComment) {
        if (parentComment == null) {
            return Comment.builder()
                    .content(content)
                    .isAnonymous(isAnonymous)
                    .user(user)
                    .post(post)
                    .build();
        }
        return Comment.builder()
                .content(content)
                .isAnonymous(isAnonymous)
                .user(user)
                .post(post)
                .parentComment(parentComment)
                .build();
    }

    public void increaseLikeNumber() {
        likeNumber++;
    }

    public void decreaseLikeNumber() {
        if (likeNumber <= 0) {
            throw new IllegalArgumentException();
        }
        likeNumber--;
    }
}
