package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.ChatMessage;
import com.ceos19.springeverytime.domain.ChatRoom;
import com.ceos19.springeverytime.repository.ChatMessageRepository;
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
}
