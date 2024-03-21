package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.ChatRoom;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository.findOne(roomId);
    }

    @Transactional
    public Long createChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getChatRoomsForUser(User user) {
        return chatRoomRepository.findAllByUser(user);
    }

    @Transactional
    public void removeChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.remove(chatRoom);
    }
}
