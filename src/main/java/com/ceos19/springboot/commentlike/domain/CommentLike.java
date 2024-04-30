package com.ceos19.springboot.commentlike.domain;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.users.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE comment_like SET deleted = true WHERE comment_like_id = ?")
@Where(clause = "deleted = false")
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

    private final boolean deleted = false;

    @Builder
    private CommentLike(Users user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}
