package com.ceos19.springeverytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.domain.Room;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoomRepositoryTest {
    @Autowired RoomRepository roomRepository;

    @Test
    void findOne() {
        //given
        Room room = new Room();

        //when
        roomRepository.save(room);

        //then
        assertEquals(room, roomRepository.findOne(room.getId()));
    }

    @Test
    void findAll() {
        //given
        Room room1 = new Room();
        Room room2 = new Room();
        Room room3 = new Room();

        //when
        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);

        List<Room> allRooms = roomRepository.findAll();
        //then

        assertEquals(3,allRooms.size());
    }
}