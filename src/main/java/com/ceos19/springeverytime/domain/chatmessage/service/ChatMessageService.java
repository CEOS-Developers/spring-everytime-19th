package com.ceos19.springeverytime.domain.chatmessage.service;

import com.ceos19.springeverytime.domain.chatmessage.domain.ChatMessage;
import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import com.ceos19.springeverytime.domain.chatmessage.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getMessagesFromChatRoom(ChatRoom chatRoom) {
        return chatMessageRepository.findAllByChatRoom(chatRoom);
    }

    @Transactional
    public ChatMessage send(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
}
