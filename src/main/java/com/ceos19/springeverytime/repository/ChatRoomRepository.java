package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.ChatRoom;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select r from ChatRoom r where r.member1 = :user or r.member2 = :user")
    public List<ChatRoom> findAllByUser(@Param("user") User user);
}
