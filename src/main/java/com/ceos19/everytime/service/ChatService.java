package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Chat;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.NO_DATA_EXISTED;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;

    public Long addChat(Chat chat) {
        chatRepository.save(chat);
        return chat.getId();
    }

    @Transactional(readOnly = true)
    public Chat findChatById(Long chatId) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isEmpty()) {
            log.error("에러 내용: 채팅 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 채팅입니다");
        }
        return optionalChat.get();
    }

    @Transactional(readOnly = true)
    public List<Chat> findChatByAuthorId(Long authorId) {
        return chatRepository.findByAuthorId(authorId);
    }

    @Transactional(readOnly = true)
    public List<Chat> findChatByChattingRoomId(Long chattingRoomId) {
        return chatRepository.findByChattingRoomId(chattingRoomId);
    }

    @Transactional(readOnly = true)
    public List<Chat> findChatBySendDate(LocalDate targetDate) {
        LocalDateTime startOfDay = LocalDateTime.of(targetDate, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(targetDate, LocalTime.MAX);

        return chatRepository.findBySentAtBetween(startOfDay, endOfDay);
    }

    public void removeChat(Long chatId) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isEmpty()) {
            log.error("에러 내용: 채팅 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 채팅입니다");
        }

        chatRepository.deleteById(chatId);
    }
}