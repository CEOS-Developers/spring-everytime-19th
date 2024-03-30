package com.ceos19.everytime.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class CreateMessageRequest {

    private Long senderId;
    private Long receiverId;
    private String content;

}

