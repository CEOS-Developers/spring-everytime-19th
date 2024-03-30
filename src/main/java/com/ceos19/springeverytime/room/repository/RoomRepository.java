package com.ceos19.springeverytime.room.repository;

import com.ceos19.springeverytime.room.domain.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room , Long> {

    List<Room> findByParticipant1_IdOrParticipant2_Id(Long userId);
}
