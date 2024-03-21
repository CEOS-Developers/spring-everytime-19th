package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final EntityManager em;

    public Long save(Post post) {
        em.persist(post);
        return post.getPostId();
    }

    public Post findOne(Long postId) {
        return em.find(Post.class, postId);
    }

    public void remove(Post post) {
        em.remove(post);
    }
}
