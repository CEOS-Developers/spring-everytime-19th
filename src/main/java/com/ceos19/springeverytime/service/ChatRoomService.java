package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.ChatRoom;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.repository.ChatRoomRepository;
import com.ceos19.springeverytime.repository.UserRepository;
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
    private final UserRepository userRepository;

    public ChatRoom findChatRoom(Long roomId) {
        Optional<ChatRoom> findChatRoom = chatRoomRepository.findById(roomId);
        if (findChatRoom.isEmpty()) {
            throw new IllegalArgumentException("해당 room ID의 room이 존재하지 않습니다.");
        }
        return findChatRoom.get();
    }

    @Transactional
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        validateDuplicatedChatRoomByUserIds(chatRoom.getMember1().getUserId(), chatRoom.getMember2().getUserId());
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> findChatRoomsForUser(User user) {
        return chatRoomRepository.findAllByUser(user);
    }

    public ChatRoom findChatRoomByUser1AndUser2(User user1, User user2) {
        Optional<ChatRoom> findChatRoom = chatRoomRepository.findChatRoomByUser1AndUser2(user1, user2);
        if (findChatRoom.isEmpty()) {
            throw new IllegalArgumentException("해당하는 채팅방이 없습니다.");
        }
        return findChatRoom.get();
    }

    @Transactional
    public void removeChatRoom(ChatRoom chatRoom) {
        // 삭제하려는 채팅방에 속한 유저가 회원탈퇴를 한 경우에는 유저가 조회되지 않음.
        // 따라서 채팅에 참여한 유저 대신 채팅방 ID 만 가지고 체크
        Optional<ChatRoom> room = chatRoomRepository.findById(chatRoom.getRoomId());
        if (room.isEmpty()) {
            throw new IllegalArgumentException("삭제하려는 채팅방이 없습니다.");
        }
        chatRoomRepository.delete(chatRoom);
    }

    private void validateDuplicatedChatRoomByUserIds(Long user1Id, Long user2Id) {
        System.out.println(userRepository);
        Optional<User> user1 = userRepository.findById(user1Id);
        Optional<User> user2 = userRepository.findById(user2Id);

        if (user1.isEmpty()) {
            throw new IllegalArgumentException("user1 ID가 존재하지 않습니다.");
        }

        if (user2.isEmpty()) {
            throw new IllegalArgumentException("user2 ID가 존재하지 않습니다.");
        }

        Optional<ChatRoom> room1 = chatRoomRepository.findChatRoomByUser1AndUser2(user1.get(), user2.get());
        Optional<ChatRoom> room2 = chatRoomRepository.findChatRoomByUser1AndUser2(user2.get(), user1.get());
        if (room1.isPresent() || room2.isPresent()) {
            throw new IllegalArgumentException("user1 user2가 대화중인 방이 이미 존재합니다.");
        }
    }
}
