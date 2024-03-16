package com.ceos19.springboot.commentlike.entity;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "comment_like")
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentLikeId;

    @OneToOne
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;


}
