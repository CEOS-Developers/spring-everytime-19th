package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.chatmessage.domain.ChatMessage;
import com.ceos19.springeverytime.domain.chatmessage.repository.ChatMessageRepository;
import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import com.ceos19.springeverytime.domain.chatroom.dto.request.ChatRoomCreateRequest;
import com.ceos19.springeverytime.domain.chatroom.dto.response.ChatRoomResponse;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {
    @Mock
    ChatRoomRepository chatRoomRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ChatMessageRepository chatMessageRepository;
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
        ChatRoomCreateRequest request = ChatRoomCreateRequest.from("hello!", 2L);
        given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(chatRoom);
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        given(userRepository.findById(2L)).willReturn(Optional.of(user2));
        given(chatMessageRepository.save(any(ChatMessage.class)))
                .willReturn(new ChatMessage("test", chatRoom, user1));

        // when
        ChatRoom createdChatRoom = chatRoomService.createChatRoom(1L, request);

        // then
        Assertions.assertEquals(chatRoom, createdChatRoom);
    }

//    @Test
//    @DisplayName("채팅방 중복 생성 에러 테스트")
//    void 채팅방_중복_생성_에러_테스트() {
//        // given
//        ChatRoom chatRoom = new ChatRoom(user1, user2);
//        given(userRepository.findById(user1.getUserId())).willReturn(Optional.of(user1));
//        given(userRepository.findById(user2.getUserId())).willReturn(Optional.of(user2));
//        given(chatRoomRepository.findChatRoomByUser1AndUser2(any(User.class), any(User.class))).willReturn(Optional.of(chatRoom));
//
//        // when
//
//        // then
//        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
//            chatRoomService.createChatRoom(new ChatRoom(user1, user2));
//        }).isInstanceOf(IllegalArgumentException.class);
//    }

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

//    @Test
//    @DisplayName("현재 로그인한 유저의 쪽지함을 조회한다.")
//    void 쪽지함_조회_테스트() {
//        // given
//        ChatRoom room1 = new ChatRoom(user1, user2);
//        ChatRoom room2 = new ChatRoom(user1, user3);
//        ChatMessage message1 = new ChatMessage("test1", room1, user1);
//        ChatMessage message2 = new ChatMessage("test1", room2, user1);
//        List<ChatRoom> chatRooms = new ArrayList<>();
//        chatRooms.add(room1);
//        chatRooms.add(room2);
//
//        given(chatRoomRepository.findAllByUserId(anyLong())).willReturn(chatRooms);
//
//        // when
//        List<ChatRoomResponse> chatRoomResponses = chatRoomService.getChatRoomsForUser(1L);
//
//        // then
//        Assertions.assertEquals(chatRoomResponses.size(), 2);
//    }
}
