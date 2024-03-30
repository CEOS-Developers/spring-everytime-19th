package com.ceos19.springeverytime.domain.chatmessage.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ChatMessageCreateRequest {
    private final String content;

    @JsonCreator
    private ChatMessageCreateRequest(String content) {
        this.content = content;
    }
}
