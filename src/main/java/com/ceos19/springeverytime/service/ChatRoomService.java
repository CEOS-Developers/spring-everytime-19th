package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.ChatRoom;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom getChatRoom(Long roomId) {
        Optional<ChatRoom> findChatRoom = chatRoomRepository.findById(roomId);
        if (findChatRoom.isEmpty()) {
            throw new IllegalArgumentException("잘못된 room ID 입니다.");
        }
        return findChatRoom.get();
    }

    @Transactional
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getChatRoomsForUser(User user) {
        return chatRoomRepository.findAllByUser(user);
    }

    @Transactional
    public void removeChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.delete(chatRoom);
    }
}
