package com.ceos19.everytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everytime.domain.Chat;
import com.ceos19.everytime.domain.ChattingRoom;
import com.ceos19.everytime.domain.User;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class ChattingRoomRepositoryTest {
    @Autowired
    ChattingRoomRepository chattingRoomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRepository chatRepository;

    @Test
    public void findTest() throws Exception{
        //given
        User user = new User("a", "a", "a", "a", "a@n.com", null,"ROLE_ADMIN");
        User user1 = new User("b", "b", "b", "b", "b@n.com", null,"ROLE_ADMIN");
        userRepository.save(user);
        userRepository.save(user1);

        ChattingRoom chattingRoom = new ChattingRoom(user,user1);
        chattingRoomRepository.save(chattingRoom);
        chatRepository.save(new Chat("a", user, chattingRoom));
        chatRepository.save(new Chat("a", user, chattingRoom));
        chatRepository.save(new Chat("a", user, chattingRoom));
        chatRepository.save(new Chat("a", user, chattingRoom));
        chatRepository.save(new Chat("a", user, chattingRoom));
        chatRepository.save(new Chat("a", user, chattingRoom));

        //when
        List<ChattingRoom> chattingRooms =
                chattingRoomRepository.findByParticipantId(user.getId());
        System.out.println("chatRepository.findAll().size() = " + chatRepository.findAll().size());

        for (ChattingRoom room : chattingRooms) {
            System.out.println("room.getId() = " + room.getId());
            chatRepository.deleteAllByChattingRoomId(room.getId());  // 채팅 삭제가 안됨
        }

        System.out.println("chatRepository.findAll().size() = " + chatRepository.findAll().size());

        //then
    }
}