package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.ChatRoom;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private final EntityManager em;

    public Long save(ChatRoom room) {
        em.persist(room);
        return room.getRoomId();
    }

    public ChatRoom findOne(Long roomId) {
        return em.find(ChatRoom.class, roomId);
    }

    public List<ChatRoom> findAllByUser(User user) {
        return em.createQuery("select r from ChatRoom r where r.member1 = :user or r.member2 = :user", ChatRoom.class)
                .setParameter("user", user)
                .getResultList();
    }

    public void remove(ChatRoom room) {
        em.remove(room);
    }
}
