//package com.ceos19.springboot.repository;
//
//import com.ceos19.springboot.comment.domain.Comment;
//import com.ceos19.springboot.comment.repository.CommentRepository;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@Transactional
//public class CommentRepositoryTest {
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Test
//    public void commentSaveTest() throws Exception {
//        //given
//        Comment comment = Comment.builder().build();
//        //when
//        commentRepository.save(comment);
//        Comment findComment = commentRepository.findById(comment.getCommentId()).orElseThrow(() -> new EntityNotFoundException("없음"));
//        //then
//        Assertions.assertThat(comment.getCommentId()).isEqualTo(findComment.getCommentId());
//
//    }
//}
