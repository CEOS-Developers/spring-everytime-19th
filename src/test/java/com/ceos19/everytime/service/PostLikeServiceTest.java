package com.ceos19.everytime.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.request.PostLikeRequestDto;
import com.ceos19.everytime.repository.PostLikeRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest {

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostLikeService postLikeService;

    @Test
    void 게시글에_좋아요를_누른다() {
        // given
        final User user = User.builder()
                .nickname("nickname")
                .build();
        final Post post = Post.builder()
                .title("test")
                .content("content")
                .isAnonymous(false)
                .writer(user)
                .build();

        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(postLikeRepository.existsByPostAndUser(any(), any()))
                .willReturn(false);

        final PostLikeRequestDto request = new PostLikeRequestDto(1L, 1L);

        // when
        postLikeService.like(request);

        // then
        verify(postLikeRepository).save(any());
    }
}
