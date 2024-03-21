package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Post;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final EntityManager em;

    public void save(Post post){
        em.persist(post);
    }

    public Optional<Post> findOne(Long id){
        return Optional.ofNullable(em.find(Post.class, id));
    }

    public List<Post> findAll(){
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    public void delete(Post post){
        em.remove(post);
    }

}
