package com.ceos19.everytime.domain.AboutPost;

import com.ceos19.everytime.domain.AboutUser.User;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Long likeNum = 0L;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private boolean isReported = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(Long commentId, String contents, Long likeNum, boolean isDeleted, boolean isReported, Comment parentComment, User user, Post post) {
        this.commentId = commentId;
        this.contents = contents;
        this.likeNum = likeNum;
        this.isDeleted = isDeleted;
        this.isReported = isReported;
        this.parentComment = parentComment;
        this.user = user;
        this.post = post;
    }

    public void deleteSubcomment(long parentCommentId) {
        this.parentComment = null;
    }

}
