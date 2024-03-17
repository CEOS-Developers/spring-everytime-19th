package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.PostLike;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository {
    private final EntityManager em;

    public void save(PostLike postLike){
        em.persist(postLike);
    }

    public Optional<PostLike> findOne(Long id){
        return Optional.ofNullable(em.find(PostLike.class, id));
    }

    public List<PostLike> findByPost(Long id){
        return em.createQuery("select l "
                + "from PostLike l "
                + "join fetch l.post p "
                + "where p.id =: postId", PostLike.class)
                .setParameter("postId", id)
                .getResultList();
    }

    public List<PostLike> findByUser(Long id){
        return em.createQuery("select l "
                + "from PostLike l "
                + "join fetch l.user u "
                + "where u.id =: id", PostLike.class)
                .setParameter("id", id)
                .getResultList();
    }

    public void delete(PostLike postLike){
        em.remove(postLike);
    }
}
