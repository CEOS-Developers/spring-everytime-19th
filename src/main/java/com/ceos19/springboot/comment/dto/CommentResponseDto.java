package com.ceos19.springboot.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private String content;
    private String username;
    private Integer contentLike;

    public CommentResponseDto(String content, String username, Integer contentLike) {
        this.content = content;
        this.username = username;
        this.contentLike = contentLike;
    }
}
