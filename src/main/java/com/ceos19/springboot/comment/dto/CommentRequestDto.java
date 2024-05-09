package com.ceos19.springboot.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private Long userId;
    private String content;
}
