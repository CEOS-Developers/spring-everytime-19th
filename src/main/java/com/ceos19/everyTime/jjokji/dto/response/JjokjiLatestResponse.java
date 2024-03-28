package com.ceos19.everyTime.jjokji.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JjokjiLatestResponse {

    private String message;
    private LocalDateTime createdAt;
    private Long chatRoomId;

    @Builder
    public JjokjiLatestResponse(String message, LocalDateTime createdAt,Long chatRoomId){
        this.message = message;
        this.createdAt = createdAt;
        this.chatRoomId = chatRoomId;
    }


}
