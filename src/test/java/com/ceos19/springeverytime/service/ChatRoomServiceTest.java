package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import com.ceos19.springeverytime.domain.chatroom.service.ChatRoomService;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.chatroom.repository.ChatRoomRepository;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {
    @Mock
    ChatRoomRepository chatRoomRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    ChatRoomService chatRoomService;

    User user1, user2, user3;

    @BeforeEach
    void 테스트_셋업() {
        user1 = EntityGenerator.generateUser("test1");
        user2 = EntityGenerator.generateUser("test2");
        user3 = EntityGenerator.generateUser("test3");
    }

    @Test
    @DisplayName("채팅방 생성 테스트")
    void 채팅방_생성_테스트() {
        // given
        ChatRoom chatRoom = new ChatRoom(user1, user2);
        given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(chatRoom);
        given(userRepository.findById(user1.getUserId())).willReturn(Optional.of(user1));
        given(userRepository.findById(user2.getUserId())).willReturn(Optional.of(user2));

        // when
        ChatRoom createdChatRoom = chatRoomService.createChatRoom(chatRoom);

        // then
        Assertions.assertEquals(chatRoom, createdChatRoom);
    }

    @Test
    @DisplayName("채팅방 중복 생성 에러 테스트")
    void 채팅방_중복_생성_에러_테스트() {
        // given
        ChatRoom chatRoom = new ChatRoom(user1, user2);
        given(userRepository.findById(user1.getUserId())).willReturn(Optional.of(user1));
        given(userRepository.findById(user2.getUserId())).willReturn(Optional.of(user2));
        given(chatRoomRepository.findChatRoomByUser1AndUser2(any(User.class), any(User.class))).willReturn(Optional.of(chatRoom));

        // when

        // then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            chatRoomService.createChatRoom(new ChatRoom(user1, user2));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("채팅방 조회 테스트")
    void 채팅방_조회_테스트() {
        // given
        ChatRoom chatRoom = new ChatRoom(user1, user2);
        given(chatRoomRepository.findById(any())).willReturn(Optional.of(chatRoom));

        // when
        ChatRoom testChatRoom = chatRoomService.findChatRoom(1L);

        // then
        Assertions.assertEquals(testChatRoom, chatRoom);
    }

    @Test
    @DisplayName("채팅방 리스트 조회 테스트")
    void 채팅방_리스트_조회_테스트() {
        // given
        List<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
        chatRooms.add(new ChatRoom(user1, user2));
        chatRooms.add(new ChatRoom(user1, user3));

        given(chatRoomRepository.findAllByUser(any(User.class))).willReturn(chatRooms);

        // when
        List<ChatRoom> testChatRooms = chatRoomService.findChatRoomsForUser(user1);

        // then
        Assertions.assertEquals(testChatRooms, chatRooms);
    }
}
