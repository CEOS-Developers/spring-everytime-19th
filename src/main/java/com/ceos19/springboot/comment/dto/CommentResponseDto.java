package com.ceos19.springboot.comment.dto;

import com.ceos19.springboot.comment.domain.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String content;
    private Long postId;
    private Long userId;

    private CommentResponseDto(String content, Long postId, Long userId) {
        this.content = content;
        this.postId = postId;
        this.userId = userId;
    }

    public static CommentResponseDto createFromComment(Comment comment){
        return new CommentResponseDto(comment.getContent(), comment.getPost().getPostId(), comment.getUser().getUserId());
    }
}
