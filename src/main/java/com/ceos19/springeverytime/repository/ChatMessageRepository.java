package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.ChatMessage;
import com.ceos19.springeverytime.domain.ChatRoom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select m from ChatMessage m where m.room = :room")
    public List<ChatMessage> findAllByChatRoom(@Param("room") ChatRoom chatRoom);
}
