package com.ceos19.springeverytime.domain.chatroom.dto.response;

import com.ceos19.springeverytime.domain.chatmessage.domain.ChatMessage;
import com.ceos19.springeverytime.domain.chatmessage.dto.response.ChatMessageResponse;
import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomResponse {
    private final Long chatRoomID;
    private final ChatMessageResponse lastChatMessage;

    public static ChatRoomResponse from(ChatRoom room) {
        List<ChatMessage> messages = room.getChatMessages();
        return new ChatRoomResponse(
                room.getRoomId(),
                ChatMessageResponse.from(messages.get(messages.size()-1))
        );
    }
}
