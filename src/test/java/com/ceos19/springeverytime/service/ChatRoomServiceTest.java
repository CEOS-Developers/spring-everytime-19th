package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.ChatRoom;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.ChatRoomRepository;
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
    @InjectMocks
    ChatRoomService chatRoomService;

    User user1, user2, user3;

    @BeforeEach
    void 테스트_셋업() {
        user1 = new User(
                "test",
                "adsfbsa234@ad",
                "nicnname",
                "kim",
                "computer",
                "20",
                "test@exmaple.com"
        );

        user2 = new User(
                "test2",
                "adsfbsa234@ad",
                "nickname2",
                "kwon",
                "data",
                "21",
                "test2@exmaple.com"
        );

        user3 = new User(
                "test3",
                "adsfbsa234@ad",
                "nickname3",
                "lee",
                "math",
                "22",
                "test3@exmaple.com"
        );
    }

    @Test
    @DisplayName("채팅방 생성 테스트")
    void 채팅방_생성_테스트() {
        // given
        ChatRoom chatRoom = new ChatRoom(user1, user2);
        given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(chatRoom);

        // when
        ChatRoom createdChatRoom = chatRoomService.createChatRoom(chatRoom);

        // then
        Assertions.assertEquals(chatRoom, createdChatRoom);
    }

    @Test
    @DisplayName("채팅방 조회 테스트")
    void 채팅방_조회_테스트() {
        // given
        ChatRoom chatRoom = new ChatRoom(user1, user2);
        given(chatRoomRepository.findById(any())).willReturn(Optional.of(chatRoom));

        // when
        ChatRoom testChatRoom = chatRoomService.getChatRoom(1L);

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
        List<ChatRoom> testChatRooms = chatRoomService.getChatRoomsForUser(user1);

        // then
        Assertions.assertEquals(testChatRooms, chatRooms);
    }
}
