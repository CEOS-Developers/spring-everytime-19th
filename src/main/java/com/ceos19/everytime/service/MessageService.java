package com.ceos19.everytime.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.domain.Message;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.request.MessageRequestDto;
import com.ceos19.everytime.repository.MessageRepository;
import com.ceos19.everytime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendMessage(final MessageRequestDto request) {
        final User sender = userRepository.findById(request.senderId())
                .orElseThrow(IllegalArgumentException::new);
        final User receiver = userRepository.findById(request.receiverId())
                .orElseThrow(IllegalArgumentException::new);

        final Message message = new Message(request.content(), sender, receiver);
        messageRepository.save(message);
    }
}
