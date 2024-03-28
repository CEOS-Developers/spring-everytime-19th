package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Chat;
import com.ceos19.everytime.domain.ChattingRoom;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.ChatRepository;
import com.ceos19.everytime.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChattingRoomService {
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChatRepository chatRepository;

    public Long addChattingRoom(ChattingRoom chattingRoom) {
        User participant1 = chattingRoom.getParticipant1();
        User participant2 = chattingRoom.getParticipant2();

        Long participant1Id = participant1.getId();
        Long participant2Id = participant2.getId();

        if (chattingRoomRepository.findByParticipant1IdOrParticipant2Id(participant1Id, participant2Id).isPresent()) {
            log.error("에러 내용: 채팅방 생성 실패 " +
                    "발생 원인: 이미 두 유저간 채팅방이 존재함");
            throw new AppException(DATA_ALREADY_EXISTED,
                    "이미 " + participant1.getName() + ", " + participant2.getName() + " 간의 채팅방이 존재합니다");
        }
        chattingRoomRepository.save(chattingRoom);
        return chattingRoom.getId();
    }

    @Transactional(readOnly = true)
    public ChattingRoom findChattingRoomById(Long chattingRoomId) {
        Optional<ChattingRoom> optionalChattingRoom = chattingRoomRepository.findById(chattingRoomId);
        if (optionalChattingRoom.isEmpty()) {
            log.error("에러 내용: 채팅방 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 채팅방입니다");
        }

        return optionalChattingRoom.get();
    }
    @Transactional(readOnly = true)
    public List<ChattingRoom> findChattingRoomByParticipantId(Long participantId) {
        return chattingRoomRepository.findByParticipantId(participantId);
    }

    @Transactional(readOnly = true)
    public ChattingRoom findChattingRoomByParticipantsId(Long participant1Id, Long participant2Id) {
        Optional<ChattingRoom> optionalChattingRoom
                = chattingRoomRepository.findByParticipant1IdOrParticipant2Id(participant1Id, participant2Id);
        if (optionalChattingRoom.isEmpty()) {
            log.error("에러 내용: 채팅방 조회 실패 " +
                    "발생 원인: 해당 유저들로 구성된 채팅방 존재 안함");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 채팅방입니다");
        }
        return optionalChattingRoom.get();
    }

    public void removeChattingRoom(Long chattingRoomId) {
        Optional<ChattingRoom> optionalChattingRoom = chattingRoomRepository.findById(chattingRoomId);
        if (optionalChattingRoom.isEmpty()) {
            log.error("에러 내용: 채팅방 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 채팅방입니다");
        }
        List<Chat> chats = chatRepository.findByChattingRoomId(chattingRoomId);
        // 연관된 Chat들 제거
        chatRepository.deleteAll(chats);
        // ChattingRoom 제거
        chattingRoomRepository.deleteById(chattingRoomId);
    }
}
