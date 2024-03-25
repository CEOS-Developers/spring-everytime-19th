package com.ceos19.springeverytime.comment.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.comment.domain.Comment;
import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
        Comment comment = Comment.builder().build();
        //when
        commentRepository.save(comment);
        //then
        Comment result = commentRepository.findById(comment.getId()).orElseThrow(IllegalStateException::new);
        assertEquals(comment, result);
    }

    @Test
    void findAll() {
        //given
        Comment comment1 = Comment.builder().build();
        Comment comment2 = Comment.builder().build();
        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        //when
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
        Comment comment1 = Comment.builder()
                .user(user).build();
        Comment comment2 = Comment.builder()
                .user(user).build();
        Comment comment3 = Comment.builder()
                .user(nonTargetUser).build();

        //when
        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        //then
        List<Comment> allCommentsByUser = commentRepository.findCommentsByUserId(user.getId());

        assertEquals(2, allCommentsByUser.size());
    }

    @Test
    void findByPost() {
        //given
        Post post = Post.builder().build();
        em.persist(post);

        Comment comment1 = Comment.builder()
                .post(post).build();
        Comment comment2 = Comment.builder()
                .post(post).build();

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        //then
        List<Comment> allCommentsByPost = commentRepository.findCommentsByPostId(post.getId());

        assertEquals(2, allCommentsByPost.size());
    }

    @Test
    void delete() {
        //given
        Comment comment = Comment.builder().build();
        //when
        commentRepository.save(comment);
        commentRepository.delete(comment);

        //then
        assertThrows(EntityNotFoundException.class, () -> commentRepository.findById(comment.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found")));
    }
}