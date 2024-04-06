package com.ceos19.springboot.service;

import com.ceos19.springboot.message.domain.Message;
import com.ceos19.springboot.message.service.MessageService;
import com.ceos19.springboot.school.domain.School;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.message.repository.MessageRepository;
import com.ceos19.springboot.school.repository.SchoolRepository;
import com.ceos19.springboot.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageServiceTest {
    @Autowired
    private MessageService messageService;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    private School saveSchool;
    private Users saveSender;
    private Users saveReceiver;
    @BeforeEach
    public void setUp() {
        School school = School.builder()
                .schoolName("홍익대학교")
                .dept("컴퓨터공학과")
                .build();
        saveSchool = schoolRepository.save(school);

        Users sender = Users.builder()
                .university(school)
                .email("email")
                .loginId("아이디")
                .nickname("sender")
                .username("sender")
                .password("비번")
                .build();
        saveSender = userRepository.save(sender);

        Users receiver = Users.builder()
                .university(school)
                .email("email")
                .loginId("아이디")
                .nickname("receiver")
                .username("receiver")
                .password("비번")
                .build();
        saveReceiver = userRepository.save(receiver);
    }
    @Test
    @DisplayName("일대일 쪽지 테스트")
    public void MessageServiceTest() throws Exception {
        //given
        String messageContent = "소통해요~";
        //when
        Long sendMessageId = messageService.sendMessage(messageContent, saveSender, saveReceiver);
        Message findMessage = messageRepository.findById(sendMessageId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID를 가진 쪽지가 없습니다"));
        //then
        Assertions.assertThat(findMessage.getMessageId()).isEqualTo(sendMessageId);
    }
}