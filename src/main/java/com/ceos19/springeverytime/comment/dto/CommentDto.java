package com.ceos19.springeverytime.comment.dto;

import com.ceos19.springeverytime.comment.domain.Comment;
import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.user.domain.User;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private Long userId;
    private Long postId;
    private Long parentCommentId;

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .id(id)
                .content(content)
                .user(user)
                .post(post)
                .build();
    }

    public Comment toEntity(User user, Post post, Comment parentComment) {
        return Comment.builder()
                .id(id)
                .content(content)
                .user(user)
                .post(post)
                .parentComment(parentComment)
                .build();
    }

}
