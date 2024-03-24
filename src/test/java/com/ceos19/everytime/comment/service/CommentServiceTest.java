package com.ceos19.everytime.comment.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceos19.everytime.comment.domain.Comment;
import com.ceos19.everytime.comment.service.CommentService;
import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.comment.dto.request.CommentWriteRequestDto;
import com.ceos19.everytime.commentlike.repository.CommentLikeRepository;
import com.ceos19.everytime.comment.repository.CommentRepository;
import com.ceos19.everytime.post.repository.PostRepository;
import com.ceos19.everytime.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Post post;

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
    }

    @Test
    void 댓글_테스트() {
        // given
        final User user1 = User.builder()
                .nickname("nickname1")
                .build();

        final CommentWriteRequestDto request = new CommentWriteRequestDto(1L, 2L, null, "댓글!", true);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user1));
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));

        // when
        commentService.writeComment(request);

        // then
        verify(commentRepository).save(any());
    }

    @Test
    void 대댓글_테스트() {
        // given
        final User user1 = User.builder()
                .nickname("nickname1")
                .build();
        final Comment parentComment = Comment.builder()
                .content("댓글")
                .isAnonymous(true)
                .user(user1)
                .post(post)
                .build();

        final CommentWriteRequestDto request = new CommentWriteRequestDto(1L, 2L, 1L, "댓글!", true);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user1));
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));
        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(parentComment));

        // when
        commentService.writeComment(request);

        // then
        verify(commentRepository).save(any());
    }

    @Test
    void 댓글을_삭제한다() {
        final Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content("content")
                .build();
        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment));
        doNothing().when(commentRepository).deleteById(anyLong());
        doNothing().when(commentLikeRepository).deleteAllByComment(any());

        // when
        commentService.delete(1L);

        // then
        verify(commentRepository).deleteById(anyLong());
        verify(commentLikeRepository).deleteAllByComment(any());
    }
}
