package com.ceos19.springboot.commentlike.domain;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.users.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentLikeId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    private CommentLike(Users user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}
