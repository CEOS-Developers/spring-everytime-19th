package com.ceos19.everytime.domain.AboutPost;

import com.ceos19.everytime.domain.AboutUser.User;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @Column(name="comment_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String contents;

    @Column(name="like_num", nullable = false)
    private Long likeNum=0L;

    @Column(name="is_deleted", nullable = false)
    private boolean isDeleted=false;

    @Column(name="is_reported", nullable = false)
    private boolean isReported=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;
}
