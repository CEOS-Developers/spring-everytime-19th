package com.ceos19.springeverytime.message.dto;

import com.ceos19.springeverytime.message.domain.Message;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessageDto {
    private Long id;
    private Long senderId;
    private String content;
    private LocalDateTime timestamp;

    public static ResponseMessageDto of(Message message){
        return ResponseMessageDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSender().getId())
                .timestamp(message.getCreatedDate())
                .build();

    }
}
