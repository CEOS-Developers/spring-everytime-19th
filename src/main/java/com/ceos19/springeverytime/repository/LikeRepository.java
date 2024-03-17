package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Like;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepository {
    private final EntityManager em;

    public void save(Like like){
        em.persist(like);
    }

    public Like findOne(Long id){
        return em.find(Like.class, id);
    }

    public List<Like> findByPost(Long id){
        return em.createQuery("select l "
                + "from Like l "
                + "join fetch Post p "
                + "where p.id =: postId", Like.class)
                .setParameter("postId", id)
                .getResultList();
    }
}
