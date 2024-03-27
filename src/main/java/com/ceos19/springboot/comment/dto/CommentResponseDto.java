package com.ceos19.springboot.comment.dto;

import com.ceos19.springboot.reply.dto.ReplyResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private String content;
    private String username;
    private Integer contentLike;
    private List<ReplyResponseDto> replyResponseDtoList;

    public CommentResponseDto(String content, String username, Integer contentLike, List<ReplyResponseDto> replyResponseDtoList) {
        this.content = content;
        this.username = username;
        this.contentLike = contentLike;
        this.replyResponseDtoList = replyResponseDtoList;
    }
}
