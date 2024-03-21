package com.ceos19.everytime.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.repository.CommentLikeRepository;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.UserRepository;
import com.ceos19.everytime.request.CommentLikeRequestDto;

@ExtendWith(MockitoExtension.class)
class CommentLikeServiceTest {

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentLikeService commentLikeService;

    @Test
    void 댓글에_좋아요를_누른다() {
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
        final Comment comment = Comment.builder()
                .content("content")
                .post(post)
                .user(user)
                .build();

        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment));
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(commentLikeRepository.existsByCommentAndUser(any(), any()))
                .willReturn(false);

        final CommentLikeRequestDto request = new CommentLikeRequestDto(1L, 1L);

        // when
        commentLikeService.like(request);

        // then
        verify(commentLikeRepository).save(any());
    }
}
