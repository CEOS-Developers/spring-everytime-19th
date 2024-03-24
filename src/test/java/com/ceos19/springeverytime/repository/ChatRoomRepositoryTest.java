package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.ChatRoom;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ChatRoomRepositoryTest {
    @Autowired EntityManager em;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired ChatMessageRepository chatMessageRepository;
    @Autowired UserRepository userRepository;

    User user1, user2;

    @BeforeEach
    void 테스트_셋업() {
        user1 = userRepository.save(EntityGenerator.generateUser("test1"));
        user2 = userRepository.save(EntityGenerator.generateUser("test2"));
    }

    @Test
    @DisplayName("채팅방 생성 테스트")
    void 채팅방_생성_테스트() {
        // given
        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(user1, user2));

        // when

        // then
        Assertions.assertThat(chatRoom.getMember1()).isSameAs(user1);
        Assertions.assertThat(chatRoom.getMember2()).isSameAs(user2);
    }

    @Test
    @DisplayName("채팅방 삭제 테스트")
    void 채팅방_삭제_테스트() {
        // given
        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(user1, user2));

        // when
        chatRoomRepository.delete(chatRoom);

        // then
        Assertions.assertThat(chatRoomRepository.findChatRoomByUser1AndUser2(user1, user2).isEmpty()).isTrue();
        Assertions.assertThat(chatRoomRepository.findChatRoomByUser1AndUser2(user2, user1).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("채팅방 삭제시 채팅 데이터 삭제 테스트")
    void 채팅방_삭제시_채팅_데이터_삭제_테스트() {
        // given
        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(user1, user2));
        chatRoom.send(user1, "hello");
        chatRoom.send(user2, "hello!");

        System.out.println("--------------------");
        System.out.println("create Chat Message Data");
        System.out.println("--------------------");

        em.flush();
        em.clear();

        // when
        System.out.println("--------------------");
        System.out.println("Delete Chat Message Data");
        System.out.println("--------------------");
        chatRoomRepository.delete(chatRoom);

        /**
         * 채팅방을 삭제하면서 채팅 메세지도 같이 삭제한다. (1+N)
         * Hibernate:
         *     delete
         *     from
         *         chat_message
         *     where
         *         message_id=?
         * Hibernate:
         *     delete
         *     from
         *         chat_message
         *     where
         *         message_id=?
         * Hibernate:
         *     delete
         *     from
         *         chat_room
         *     where
         *         room_id=?
         */

        // then
        Assertions.assertThat(chatRoomRepository.findChatRoomByUser1AndUser2(user1, user2).isEmpty()).isTrue();
        Assertions.assertThat(chatRoomRepository.findChatRoomByUser1AndUser2(user2, user1).isEmpty()).isTrue();
        Assertions.assertThat(chatMessageRepository.findAllByChatRoom(chatRoom).isEmpty()).isTrue();
    }
}
