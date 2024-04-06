package com.ceos19.springeverytime.domain.chatmessage.service;

import com.ceos19.springeverytime.domain.chatmessage.domain.ChatMessage;
import com.ceos19.springeverytime.domain.chatmessage.dto.request.ChatMessageCreateRequest;
import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import com.ceos19.springeverytime.domain.chatmessage.repository.ChatMessageRepository;
import com.ceos19.springeverytime.domain.chatroom.repository.ChatRoomRepository;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import com.ceos19.springeverytime.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ceos19.springeverytime.global.exception.ExceptionCode.NOT_FOUND_CHAT_ROOM_ID;
import static com.ceos19.springeverytime.global.exception.ExceptionCode.NOT_FOUND_USER_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatMessage> getMessagesFromChatRoom(ChatRoom chatRoom) {
        return chatMessageRepository.findAllByChatRoom(chatRoom);
    }

    @Transactional
    public void sendMessage(final Long roomId, final Long userId, final ChatMessageCreateRequest request) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CHAT_ROOM_ID));
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));

        chatMessageRepository.save(new ChatMessage(
                request.getContent(),
                room,
                sender
        ));
    }
}
