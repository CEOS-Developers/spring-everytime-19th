package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.AboutMessage.Message;
import com.ceos19.everytime.domain.AboutUser.User;
import com.ceos19.everytime.repository.MessageRepository;
import com.ceos19.everytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    // 1:1 쪽지 기능
    @Transactional
    public Message sendChatMessage(Long requestUserId, Long responseUserId, String contents) {
        // 메시지 전송자와 수신자 조회
        User requestUser = userRepository.findById(requestUserId)
                .orElseThrow(() -> new IllegalArgumentException("There is no RequestUser with id: " + requestUserId));
        User responseUser = userRepository.findById(responseUserId)
                .orElseThrow(() -> new IllegalArgumentException("There is no ResponseUser with id: " + responseUserId));

        // 메시지 객체 생성
        Message message = Message.builder()
                .contents(contents)
                .sendAt(Timestamp.valueOf(LocalDateTime.now()))
                .user1(requestUser)
                .user2(responseUser)
                .build();

        // 메시지 저장
        return messageRepository.save(message);
    }
}
