package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.ChatMessage;
import com.ceos19.springeverytime.domain.ChatRoom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {
    private final EntityManager em;

    public List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom) {
        return em.createQuery("select m from ChatMessage m where m.room = :room", ChatMessage.class)
                .setParameter("room", chatRoom)
                .getResultList();
    }
}
