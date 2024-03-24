package com.ceos19.everyTime.jjokji.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MessageDto {
    private String message;
    private Long senderId;
    private Long receiverId;

}
