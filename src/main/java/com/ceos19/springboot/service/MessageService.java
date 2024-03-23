package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.Message;
import com.ceos19.springboot.domain.Users;
import com.ceos19.springboot.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    @Transactional
    public Long sendMessage(String content, Users sender, Users receiver) {
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .build();
        Message saveMessage = messageRepository.save(message);
        return saveMessage.getMessageId();
    }
}
