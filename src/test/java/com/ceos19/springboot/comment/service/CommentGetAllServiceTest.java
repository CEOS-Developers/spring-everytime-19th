package com.ceos19.springboot.comment.service;

import com.ceos19.springboot.comment.dto.CommentResponseDto;
import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentGetAllServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void getAllComments() {
        // Given
        Post post = new Post("1","1",true);
        postRepository.save(post);

        Comment comment1 = new Comment();
        comment1.setContent("Test comment 1");
        comment1.setPost(post);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Test comment 2");
        comment2.setPost(post);
        commentRepository.save(comment2);

        // When
        List<CommentResponseDto> comments = commentService.getAllComments();

        // Then
        assertEquals(2, comments.size());
        assertEquals("Test comment 1", comments.get(0).getContent());
        assertEquals("Test comment 2", comments.get(1).getContent());
    }


}