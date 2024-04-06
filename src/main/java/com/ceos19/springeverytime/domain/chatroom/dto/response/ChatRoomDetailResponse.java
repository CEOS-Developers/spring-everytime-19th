package com.ceos19.springeverytime.domain.chatroom.dto.response;

import com.ceos19.springeverytime.domain.chatmessage.dto.response.ChatMessageResponse;
import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomDetailResponse {
    private final Long roomId;
    private final List<ChatMessageResponse> messages;

    public static ChatRoomDetailResponse from(ChatRoom room) {
        List<ChatMessageResponse> responses = room.getChatMessages()
                .stream()
                .map(ChatMessageResponse::from)
                .toList();

        return new ChatRoomDetailResponse(
                room.getRoomId(),
                responses
        );
    }
}
