package com.ceos19.springeverytime.domain.chatroom.service;

import static com.ceos19.springeverytime.global.exception.ExceptionCode.NOT_FOUND_CHAT_ROOM_ID;
import static com.ceos19.springeverytime.global.exception.ExceptionCode.NOT_FOUND_USER_ID;

import com.ceos19.springeverytime.domain.chatmessage.domain.ChatMessage;
import com.ceos19.springeverytime.domain.chatmessage.repository.ChatMessageRepository;
import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import com.ceos19.springeverytime.domain.chatroom.dto.request.ChatRoomCreateRequest;
import com.ceos19.springeverytime.domain.chatroom.dto.response.ChatRoomDetailResponse;
import com.ceos19.springeverytime.domain.chatroom.dto.response.ChatRoomResponse;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.chatroom.repository.ChatRoomRepository;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import com.ceos19.springeverytime.global.exception.BadRequestException;
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
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatRoom findChatRoom(Long roomId) {
        Optional<ChatRoom> findChatRoom = chatRoomRepository.findById(roomId);
        if (findChatRoom.isEmpty()) {
            throw new IllegalArgumentException("해당 room ID의 room이 존재하지 않습니다.");
        }
        return findChatRoom.get();
    }

    @Transactional
    public ChatRoom createChatRoom(Long userId, ChatRoomCreateRequest request) {
        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));

        User toUser = userRepository.findById(request.getToUserId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));

        validateDuplicatedChatRoomByUserIds(userId, request.getToUserId());
        ChatRoom newChatRoom = chatRoomRepository.save(new ChatRoom(
                fromUser,
                toUser
        ));

        chatMessageRepository.save(new ChatMessage(
                request.getMessage(),
                newChatRoom,
                fromUser
        ));
        return newChatRoom;
    }

    public List<ChatRoomResponse> getChatRoomsForUser(Long userId) {
        final List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(userId);
        return chatRooms.stream().map(ChatRoomResponse::from).toList();
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

    public ChatRoomDetailResponse getCharRoomDetail(final Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CHAT_ROOM_ID));

        return ChatRoomDetailResponse.from(room);
    }

    @Transactional
    public void delete(Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            throw new BadRequestException(NOT_FOUND_CHAT_ROOM_ID);
        }
        chatRoomRepository.deleteById(roomId);
    }
}
