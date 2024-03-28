package com.ceos19.springboot.postLike.service;

import com.ceos19.springboot.common.RestApiException;
import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.postlike.entity.postLike;
import com.ceos19.springboot.postlike.repository.PostLikeRepository;
import com.ceos19.springboot.postlike.service.PostLikeService;
import com.ceos19.springboot.user.entity.User;
import com.ceos19.springboot.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostLikeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @InjectMocks
    private PostLikeService postLikeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postLikeCreate_Success() {

        User user = new User();
        user.setUsername("testUser");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        Post post = new Post();
        post.setPostId(1L);

        // Mocking repositories
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postLikeRepository.findByUserAndComment(user, post)).thenReturn(Optional.empty());

        ApiResponseDto<SuccessResponse> response = postLikeService.postLikeCreate(userDetails, 1L);

        verify(postLikeRepository, times(1)).save(any(postLike.class));

        assertEquals(HttpStatus.OK, response.getResponse().getStatus());
        assertEquals("commentLike Create Success", response.getError().getMessage());
    }

    @Test
    void postLikeCreate_UserNotFound() {

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("nonExistingUser");

        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(RestApiException.class, () -> postLikeService.postLikeCreate(userDetails, 1L),
                "Expected RestApiException to be thrown");

        verify(postLikeRepository, never()).save(any(postLike.class));
    }

    @Test
    void postLikeCreate_PostNotFound() {

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestApiException.class, () -> postLikeService.postLikeCreate(userDetails, 1L),
                "Expected RestApiException to be thrown");

        verify(postLikeRepository, never()).save(any(postLike.class));
    }

    @Test
    void postLikeCreate_AlreadyExists() {

        User user = new User();
        user.setUsername("testUser");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        Post post = new Post();
        post.setPostId(1L);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postLikeRepository.findByUserAndComment(user, post)).thenReturn(Optional.of(new postLike()));

        assertThrows(RestApiException.class, () -> postLikeService.postLikeCreate(userDetails, 1L),
                "Expected RestApiException to be thrown");

        verify(postLikeRepository, never()).save(any(postLike.class));
    }
}
