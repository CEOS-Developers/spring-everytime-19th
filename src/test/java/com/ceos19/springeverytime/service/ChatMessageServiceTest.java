package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.chatmessage.domain.ChatMessage;
import com.ceos19.springeverytime.domain.chatmessage.service.ChatMessageService;
import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.chatmessage.repository.ChatMessageRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {
    @Mock
    ChatMessageRepository chatMessageRepository;

    @InjectMocks
    ChatMessageService chatMessageService;

    User user1, user2;
    ChatRoom chatRoom;

    @BeforeEach
    void 테스트_셋업() {
        user1 = EntityGenerator.generateUser("test1");
        user2 = EntityGenerator.generateUser("test2");
        chatRoom = new ChatRoom(user1, user2);
    }

    @Test
    @DisplayName("채팅 내역 조회 테스트")
    void 채팅_내역_조회_테스트() {
        // given
        ChatMessage chatMessage1 = ChatMessage.builder().room(chatRoom).content("안녕하세요").sender(user1).build();
        ChatMessage chatMessage2 = ChatMessage.builder().room(chatRoom).content("안녕하세요").sender(user2).build();

        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(chatMessage1);
        messageList.add(chatMessage2);

        given(chatMessageRepository.findAllByChatRoom(any(ChatRoom.class))).willReturn(messageList);

        // when
        List<ChatMessage> testMessageList = chatMessageService.getMessagesFromChatRoom(chatRoom);

        // then
        Assertions.assertEquals(testMessageList, messageList);
    }
}
