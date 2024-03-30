package com.ceos19.springeverytime.room.service;

import com.ceos19.springeverytime.room.domain.Room;
import com.ceos19.springeverytime.room.dto.RoomDto;
import com.ceos19.springeverytime.room.repository.RoomRepository;
import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createRoom(RoomDto roomDto){
        User participant1 = userRepository.findUserById(roomDto.getParticipant1Id())
                .orElseThrow(EntityNotFoundException::new);
        User participant2 = userRepository.findUserById(roomDto.getParticipant2Id())
                .orElseThrow(EntityNotFoundException::new);

        roomRepository.save(roomDto.toEntity(participant1,participant2));
    }

    public List<Room> getAllRooms(Long userId){
        return roomRepository.findByParticipant1_IdOrParticipant2_Id(userId);
    }

    public RoomDto getRoomById(Long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);
        return RoomDto.of(room);
    }

    @Transactional
    public void deleteRoomById(Long roomId){
        roomRepository.deleteById(roomId);
    }
}
