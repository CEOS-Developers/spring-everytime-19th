package com.ceos19.springeverytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CommentRepositoryTest {

    @Autowired private CommentRepository commentRepository;
    @Autowired private EntityManager em;

    @Test
    void FindOne() {
        //given
        Comment comment = new Comment();
        //when
        commentRepository.save(comment);
        //then
        Assertions.assertEquals(comment, commentRepository.findOne(comment.getId()));
    }

    @Test
    void findAll() {
        //given
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        List<Comment> allComments = commentRepository.findAll();
        //then

        assertEquals(2, allComments.size());
    }

    @Test
    void findByUser() {
        //given
        User user = User.builder()
                .build();
        User nonTargetUser = User.builder()
                .build();
        em.persist(user);
        em.persist(nonTargetUser);
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        Comment comment3 = new Comment();
        comment1.setUser(user);
        comment2.setUser(user);
        comment3.setUser(nonTargetUser);

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //then
        List<Comment> allCommentsByUser = commentRepository.findByUser(user.getId());

        assertEquals(2, allCommentsByUser.size());
    }

    @Test
    void findByPost() {
        //given
        Post post = new Post();
        em.persist(post);
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        comment1.setPost(post);
        comment2.setPost(post);

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        //then
        List<Comment> allCommentsByPost = commentRepository.findByPost(post.getId());

        assertEquals(2, allCommentsByPost.size());
    }

    @Test
    void delete() {
        //given
        Comment comment = new Comment();
        //when
        commentRepository.save(comment);
        commentRepository.delete(comment);

        //then
        assertThrows(EntityNotFoundException.class, () -> commentRepository.findOne(comment.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found")));
    }
}