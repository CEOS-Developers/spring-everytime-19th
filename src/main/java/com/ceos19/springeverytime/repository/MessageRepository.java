package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Message;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepository {
    private final EntityManager em;

    public void save(Message message) {
        em.persist(message);
    }

    public Message findOne(Long id){
        return em.find(Message.class,id);
    }

    public List<Message> findByUser(Long id){
        return em.createQuery("select m "
                + "from Message m "
                + "join fetch m.sender u "
                + "where u.id =: userId",Message.class)
                .setParameter("userId",id)
                .getResultList();
    }

    public List<Message> findByRoom(Long id){
        return em.createQuery("select m "
                + "from Message m "
                + "join fetch m.room r "
                + "where r.id =: roomId",Message.class)
                .setParameter("roomId",id)
                .getResultList();
    }
}
