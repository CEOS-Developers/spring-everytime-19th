package com.ceos19.springboot.message.dto;

import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.post.dto.PostResponseDto;
import com.ceos19.springboot.users.domain.Users;
import lombok.Getter;

@Getter
public class MessageResponseDto {
    private Long senderId;
    private Long receiverId;
    private String content;

    private MessageResponseDto(Long senderId, Long receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }


    public static MessageResponseDto createFromMessage(Long senderId,Long receiverId,String content) {
        return new MessageResponseDto(senderId,receiverId,content);
    }
}
