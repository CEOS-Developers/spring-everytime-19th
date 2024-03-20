package com.ceos19.springboot.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private Long postId;
    private String content;



    public CommentRequestDto(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
