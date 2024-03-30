package com.ceos19.springeverytime.domain.chatmessage.dto.response;

import com.ceos19.springeverytime.domain.chatmessage.domain.ChatMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageResponse {
    // 유저 정보를 어떻게 전달해야 할까?
    private final String content;
    private final LocalDateTime sendDate;

    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
                message.getContent(),
                message.getCreateDate()
        );
    }
}
