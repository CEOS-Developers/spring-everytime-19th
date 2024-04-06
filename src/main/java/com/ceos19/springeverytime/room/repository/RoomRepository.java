package com.ceos19.springeverytime.room.repository;

import com.ceos19.springeverytime.room.domain.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room , Long> {

    @Query("select r "
            + "from Room r "
            + "where r.participant1.id = :id or r.participant2.id = : id")
    List<Room> findByParticipant1_IdOrParticipant2_Id(Long id);
}
