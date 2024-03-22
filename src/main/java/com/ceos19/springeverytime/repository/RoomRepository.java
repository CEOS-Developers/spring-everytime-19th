package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room , Long> {
}
