package com.ceos19.springeverytime.room.repository;

import com.ceos19.springeverytime.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room , Long> {

}
