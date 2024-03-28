package com.ceos19.everyTime.jjokji.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JjokjiResponse {
    private String message;
    private LocalDateTime createdAt;
    private Long senderId;

    @Builder
    JjokjiResponse(String message, LocalDateTime createdAt,Long senderId){
        this.message = message;
        this.createdAt = createdAt;
        this.senderId = senderId;
    }

}
