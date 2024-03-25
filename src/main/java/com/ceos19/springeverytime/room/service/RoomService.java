package com.ceos19.springeverytime.room.service;

import com.ceos19.springeverytime.room.domain.Room;
import com.ceos19.springeverytime.room.dto.RoomDto;
import com.ceos19.springeverytime.room.repository.RoomRepository;
import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {
    private RoomRepository roomRepository;
    private UserService userService;

    @Transactional
    public void createRoom(RoomDto roomDto){
        User participant1 = userService.getUser(roomDto.getParticipant1Id());
        User participant2 = userService.getUser(roomDto.getParticipant2Id());

        roomRepository.save(roomDto.toEntity(participant1,participant2));
    }

    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

    public Room getRoomById(Long roomId){
        return roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void deleteRoomById(Long roomId){
        roomRepository.deleteById(roomId);
    }
}
