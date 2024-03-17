package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Comment;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;

    public void save(Comment comment){
        em.persist(comment);
    }

    public Comment findOne(Long id){
        return em.find(Comment.class,id);
    }

    public List<Comment> findAll(){
        return em.createQuery("select c from Comment c", Comment.class)
                .getResultList();
    }

    public List<Comment> findByUser(Long id){
        return em.createQuery("select c "
                + "from Comment c "
                + "join fetch User u "
                + "where u.id =: userId",
                Comment.class)
                .setParameter("userId",id)
                .getResultList();
    }

    public List<Comment> findByPost(Long id){
        return em.createQuery("select c "
                + "from Comment c "
                + "join fetch Post p "
                + "where p.id =: postId", Comment.class)
                .setParameter("postId",id)
                .getResultList();
    }

}
