package com.ceos19.springboot.reply.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyRequestDto {
    private String commentId;
    private String content;
    private Integer likes;

    public ReplyRequestDto(String commentId, String content, Integer likes) {
        this.commentId = commentId;
        this.content = content;
        this.likes = likes;
    }
}
