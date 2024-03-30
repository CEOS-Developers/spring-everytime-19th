package com.ceos19.springeverytime.domain.chatroom.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomCreateRequest {
    private final String message;
    private final Long toUserId;

    public static ChatRoomCreateRequest from(final String message, final Long toUserId) {
        return new ChatRoomCreateRequest(message, toUserId);
    }
}
