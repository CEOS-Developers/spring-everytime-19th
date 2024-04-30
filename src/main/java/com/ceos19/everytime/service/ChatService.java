package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Chat;
import com.ceos19.everytime.domain.ChattingRoom;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.AddChatRequest;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.repository.ChatRepository;
import com.ceos19.everytime.repository.ChattingRoomRepository;
import com.ceos19.everytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.NO_DATA_EXISTED;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;

    public Long addChat(Chat chat) {
        chatRepository.save(chat);
        return chat.getId();
    }

    public Long addChat(Long chattingRoomId, AddChatRequest request) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId).orElseThrow(() -> {
            log.error("에러 내용: 채팅 등록 실패 " +
                    "발생 원인: 존재하지 않는 ChattingRomm의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 채팅방입니다");
        });

        User author = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            log.error("에러 내용: 채팅 등록 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });

        Long authorId = author.getId();
        // author가 chatting 방에 속하지 않은 경우 에러 발생
        if (!Objects.equals(chattingRoom.getParticipant1().getId(), authorId)
                && !Objects.equals(chattingRoom.getParticipant2().getId(), authorId)) {
            log.error("에러 내용: 채팅 등록 실패 " +
                    "발생 원인: 채팅방에 속하지 않은 유저가 채팅 등록 시도");
            throw new AppException(ErrorCode.NOT_INCLUDED_USER_ACCESS, "채팅방에 속하지 않은 유저입니다");
        }

        Chat chat = new Chat(request.getContent(), author, chattingRoom);
        chatRepository.save(chat);

        return chat.getId();
    }

    @Transactional(readOnly = true)
    public Chat findChatById(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(() -> {
            log.error("에러 내용: 채팅 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 채팅입니다");
        });
    }

    @Transactional(readOnly = true)
    public List<Chat> findChatByAuthorId(Long authorId) {
        userRepository.findById(authorId).orElseThrow(() -> {
            log.error("에러 내용: 채팅 조회 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
        return chatRepository.findByAuthorId(authorId);
    }

    @Transactional(readOnly = true)
    public List<Chat> findChatByChattingRoomId(Long chattingRoomId) {
        chattingRoomRepository.findById(chattingRoomId).orElseThrow(() -> {
            log.error("에러 내용: 채팅 조회 실패 " +
                    "발생 원인: 존재하지 않는 ChattingRoom의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 채팅방입니다");
        });
        return chatRepository.findByChattingRoomId(chattingRoomId);
    }

    @Transactional(readOnly = true)
    public List<Chat> findChatByChattingRoomIdAndSendDate(Long chattingRoomId, LocalDate targetDate) {
        chattingRoomRepository.findById(chattingRoomId).orElseThrow(() -> {
            log.error("에러 내용: 채팅 조회 실패 " +
                    "발생 원인: 존재하지 않는 ChattingRoom의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 채팅방입니다");
        });

        LocalDateTime startOfDay = LocalDateTime.of(targetDate, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(targetDate, LocalTime.MAX);

        return chatRepository.findByChattingRoomIdAndSentAtBetween(chattingRoomId, startOfDay, endOfDay);
    }

    public void removeChat(Long chatId) {
        chatRepository.findById(chatId).orElseThrow(() -> {
            log.error("에러 내용: 채팅 제거 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 채팅입니다");
        });

        chatRepository.deleteById(chatId);
    }
}