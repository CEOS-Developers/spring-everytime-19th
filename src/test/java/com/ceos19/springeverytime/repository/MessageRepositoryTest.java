package com.ceos19.springeverytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.domain.Message;
import com.ceos19.springeverytime.domain.Room;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
        Message message = new Message();
        //when
        messageRepository.save(message);
        //then
        Assertions.assertEquals(message, messageRepository.findOne(message.getId()));
    }

    @Test
    void findByUser() {
        //given
        User user = User.builder()
                .build();
        em.persist(user);
        Message message1 = new Message();
        Message message2 = new Message();
        //when
        message1.setSender(user);
        message2.setSender(user);
        messageRepository.save(message1);
        messageRepository.save(message2);
        List<Message> allMessages = messageRepository.findByUser(user.getId());
        //then
        assertEquals(2, allMessages.size());
    }

    @Test
    void findByRoom() {
        //given
        Room room = new Room();
        em.persist(room);
        Message message1 = new Message();
        Message message2 = new Message();
        //when
        message1.setRoom(room);
        message2.setRoom(room);
        messageRepository.save(message1);
        messageRepository.save(message2);
        //then
        List<Message> allMessagesByPost = messageRepository.findByRoom(room.getId());

        assertEquals(2,allMessagesByPost.size());
    }
}