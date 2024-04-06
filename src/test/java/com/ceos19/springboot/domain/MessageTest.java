package com.ceos19.springboot.domain;

import com.ceos19.springboot.message.domain.Message;
import com.ceos19.springboot.message.repository.MessageRepository;
import com.ceos19.springboot.users.repository.UserRepository;
import com.ceos19.springboot.users.domain.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Test
    @DisplayName("쪽지 기능 테스트")
    public void MessageTest() throws Exception {
        //given
        Users sender = Users.builder()
                //.university("홍익대")
                .email("sender_email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("sender")
                .password("비번")
                .build();

        Users receiver = Users.builder()
                //.university("홍익대")
                .email("receiver_email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("receiver")
                .password("비번")
                .build();

        Users saveSender = userRepository.save(sender);
        Users saveReceiver = userRepository.save(receiver);
        //when
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content("1:1 쪽지 내용")
                .build();

        Message saveMessage = messageRepository.save(message);
        //then
    }
}