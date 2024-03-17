package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    final private EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment findOne(Long commentId) {
        return em.find(Comment.class, commentId);
    }

    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    public List<Comment> findAllByPost(Post post) {
        return em.createQuery("select c from Comment c where c.post = :post", Comment.class)
                .setParameter("post", post)
                .getResultList();
    }
}
