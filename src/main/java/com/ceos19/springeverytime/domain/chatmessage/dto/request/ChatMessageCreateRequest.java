package com.ceos19.springeverytime.domain.chatmessage.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageCreateRequest {
    private final String content;
}
