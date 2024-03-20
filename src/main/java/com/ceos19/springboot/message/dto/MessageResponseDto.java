package com.ceos19.springboot.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageResponseDto {
    private String title;
    private String content;
    private String sender;
    private String receiver;

    public MessageResponseDto(String title, String content, String sender, String receiver) {
        this.title = title;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }
}
