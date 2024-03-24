package com.ceos19.everytime.message.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.message.domain.Message;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.message.dto.request.MessageReadRequestDto;
import com.ceos19.everytime.message.dto.request.MessageRequestDto;
import com.ceos19.everytime.message.dto.response.MessageResponseDto;
import com.ceos19.everytime.message.repository.MessageRepository;
import com.ceos19.everytime.user.repository.UserRepository;

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

    @Transactional(readOnly = true)
    public List<MessageResponseDto> readMessage(final MessageReadRequestDto request) {
        final User receiver = userRepository.findById(request.receiverId())
                .orElseThrow(IllegalArgumentException::new);
        final List<Message> messages = messageRepository.findByIdAndReceiver(request.messageId(), receiver);
        return messages.stream()
                .map(message -> new MessageResponseDto(message.getSender().getNickname(), message.getContent(),
                        message.getTransferTime()))
                .toList();
    }
}
