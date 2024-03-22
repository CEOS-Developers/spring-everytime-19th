package com.ceos19.springeverytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.room.domain.Room;
import com.ceos19.springeverytime.room.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoomRepositoryTest {
    @Autowired
    RoomRepository roomRepository;

    @Test
    void findOne() {
        //given
        Room room = Room.builder().build();

        //when
        roomRepository.save(room);

        //then
        Room result = roomRepository.findById(room.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(room, result);
    }

    @Test
    void findAll() {
        //given
        Room room1 = Room.builder().build();
        Room room2 = Room.builder().build();
        Room room3 = Room.builder().build();

        //when
        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);

        List<Room> allRooms = roomRepository.findAll();
        //then

        assertEquals(3,allRooms.size());
    }
}