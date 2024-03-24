package com.ceos19.everytime.commentlike.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceos19.everytime.comment.domain.Comment;
import com.ceos19.everytime.commentlike.domain.CommentLike;
import com.ceos19.everytime.commentlike.service.CommentLikeService;
import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.commentlike.repository.CommentLikeRepository;
import com.ceos19.everytime.comment.repository.CommentRepository;
import com.ceos19.everytime.user.repository.UserRepository;
import com.ceos19.everytime.commentlike.dto.request.CommentLikeRequestDto;

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

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .nickname("nickname")
                .build();
        post = Post.builder()
                .title("test")
                .content("content")
                .isAnonymous(false)
                .writer(user)
                .build();
        comment = Comment.builder()
                .content("content")
                .post(post)
                .user(user)
                .build();
    }

    @Test
    void 댓글에_좋아요를_누른다() {
        // given
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

    @Test
    void 댓글에_좋아요를_취소한다() {
        // given
        final CommentLike commentLike = new CommentLike(user, comment);

        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment));
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(commentLikeRepository.findByCommentAndUser(any(), any()))
                .willReturn(Optional.of(commentLike));

        final CommentLikeRequestDto request = new CommentLikeRequestDto(1L, 1L);
        commentLikeService.like(request);

        // when
        commentLikeService.cancel(request);

        // then
        verify(commentLikeRepository).delete(any());
    }
}
