package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.like.Like;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepository {
    private final EntityManager em;

    public void save(Like like) {
        em.persist(like);
    }

    public Like findOne(Long likeId) {
        return em.find(Like.class, likeId);
    }

    public List<Like> findAll() {
        return em.createQuery("select l from Like l", Like.class).getResultList();
    }
}
