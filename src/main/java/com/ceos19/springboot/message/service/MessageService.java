package com.ceos19.springboot.message.service;

import com.ceos19.springboot.message.domain.Message;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.message.repository.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Message> retreiveMessage(Users sender) {
        return messageRepository.findBySender(sender)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 보낸 메시지가 없습니다"));
    }
}
