package com.ceos19.springeverytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.domain.Message;
import com.ceos19.springeverytime.domain.Room;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MessageRepositoryTest {
    @Autowired private MessageRepository messageRepository;
    @Autowired private EntityManager em;
    
    @Test
    void FindOne() {
        //given
        Message message = Message.builder().build();
        //when
        messageRepository.save(message);
        //then
        Message result = messageRepository.findById(message.getId()).orElseThrow(IllegalStateException::new);
        assertEquals(message, result);
    }

    @Test
    void findByUser() {
        //given
        User user = User.builder()
                .build();
        em.persist(user);
        Message message1 = Message.builder().
                sender(user)
                .build();
        Message message2 = Message.builder().
                sender(user)
                .build();

        //when
        messageRepository.save(message1);
        messageRepository.save(message2);
        List<Message> allMessages = messageRepository.findMessagesBySender_Id(user.getId());
        //then
        assertEquals(2, allMessages.size());
    }

    @Test
    void findByRoom() {
        //given
        Room room = Room.builder().build();
        em.persist(room);
        Message message1 = Message.builder()
                .room(room).build();
        Message message2 = Message.builder()
                .room(room).build();
        //when
        messageRepository.save(message1);
        messageRepository.save(message2);
        //then
        List<Message> allMessagesByPost = messageRepository.findMessagesByRoom_Id(room.getId());

        assertEquals(2,allMessagesByPost.size());
    }
}