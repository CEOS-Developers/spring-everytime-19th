package com.ceos19.springboot.comment.service;
import com.ceos19.springboot.comment.dto.CommentRequestDto;
import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.comment.service.CommentService;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.user.entity.User;
import com.ceos19.springboot.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommentCreateServiceTest {

    @Test
    public void testCreateComment() {
        // Mock repositories
        UserRepository userRepository = mock(UserRepository.class);
        PostRepository postRepository = mock(PostRepository.class);
        CommentRepository commentRepository = mock(CommentRepository.class);

        // Create a mock user and post
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        Post post = new Post();
        post.setPostId(1L);

        // Mock UserRepository behavior
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Mock PostRepository behavior
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Create a CommentService instance with mocked repositories
        CommentService commentService = new CommentService(commentRepository,  postRepository,userRepository);

        // Create a CommentRequestDto
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContent("Test comment");

        // Test the createComment method
        commentService.createComment(userDetails, commentRequestDto, 1L);

        // Verify that the userRepository.findByUsername method was called once with "testUser"
        verify(userRepository, times(1)).findByUsername("testUser");

        // Verify that the postRepository.findById method was called once with 1L
        verify(postRepository, times(1)).findById(1L);

        // Verify that the commentRepository.save method was called once
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}
