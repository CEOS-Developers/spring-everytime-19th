//package com.ceos19.springboot.repository;
//
//import com.ceos19.springboot.comment.domain.Comment;
//import com.ceos19.springboot.comment.repository.CommentRepository;
//import com.ceos19.springboot.message.domain.Message;
//import com.ceos19.springboot.message.repository.MessageRepository;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@Transactional
//public class MessageRepositoryTest {
//    @Autowired
//    private MessageRepository messageRepository;
//    @Test
//    public void commentSaveTest() throws Exception {
//        //given
//        Message message = Message.builder().build();
//        //when
//        messageRepository.save(message);
//        Message findMessage = messageRepository.findById(message.getMessageId()).orElseThrow(() -> new EntityNotFoundException("없음"));
//        //then
//        Assertions.assertThat(message.getMessageId()).isEqualTo(findMessage.getMessageId());
//    }
//}
