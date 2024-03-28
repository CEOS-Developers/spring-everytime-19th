package com.ceos19.springboot.reply.entity;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    private String content;

    private Integer likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Reply(String content,  Comment comment, User user) {
        this.content = content;
        this.comment = comment;
        this.user = user;
    }
}
