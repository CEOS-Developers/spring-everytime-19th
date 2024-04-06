package com.ceos19.springboot.comment.dto;

import com.ceos19.springboot.comment.domain.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String content;
    private Long postId;

    private CommentResponseDto(String content, Long postId) {
        this.content = content;
        this.postId = postId;
    }

    public static CommentResponseDto createFromComment(Comment comment){
        return new CommentResponseDto(comment.getContent(), comment.getPost().getPostId());
    }
}
