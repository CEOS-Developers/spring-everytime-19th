package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.PostLike;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository {
    private final EntityManager em;

    public void save(PostLike postLike){
        em.persist(postLike);
    }

    public PostLike findOne(Long id){
        return em.find(PostLike.class, id);
    }

    public List<PostLike> findByPost(Long id){
        return em.createQuery("select l "
                + "from PostLike l "
                + "join fetch Post p "
                + "where p.id =: postId", PostLike.class)
                .setParameter("postId", id)
                .getResultList();
    }
}
