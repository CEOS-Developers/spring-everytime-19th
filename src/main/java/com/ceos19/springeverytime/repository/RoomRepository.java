package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Room;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomRepository {
    private final EntityManager em;

    public void save(Room room) {
        em.persist(room);
    }

    public Room findOne(Long id){
        return em.find(Room.class, id);
    }

    public List<Room> findAll(){
        return em.createQuery("select r "
                + "from Room r",Room.class)
                .getResultList();
    }
}
