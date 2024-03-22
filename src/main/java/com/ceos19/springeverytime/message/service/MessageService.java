package com.ceos19.springeverytime.message.service;

import com.ceos19.springeverytime.room.domain.Room;
import com.ceos19.springeverytime.message.dto.MessageDto;
import com.ceos19.springeverytime.message.repository.MessageRepository;
import com.ceos19.springeverytime.room.repository.RoomRepository;
import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;

    public void createMessage(MessageDto messageDto) {
        User user = userRepository.findUserById(messageDto.getSenderId())
                .orElseThrow(EntityNotFoundException::new);
        Room room = roomRepository.findById(messageDto.getRoomId())
                .orElseThrow(EntityNotFoundException::new);

        messageRepository.save(messageDto.toEntity(room, user));
    }
}
