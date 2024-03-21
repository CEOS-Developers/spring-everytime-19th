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

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.request.CommentWriteRequestDto;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void 댓글_테스트() {
        // given
        final User user = User.builder()
                .nickname("nickname")
                .build();
        final User user1 = User.builder()
                .nickname("nickname1")
                .build();
        final Board board = Board.builder()
                .name("자유 게시판")
                .build();
        final Post post = Post.builder()
                .board(board)
                .writer(user)
                .content("content")
                .title("title")
                .isAnonymous(true)
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
        final User user = User.builder()
                .nickname("nickname")
                .build();
        final User user1 = User.builder()
                .nickname("nickname1")
                .build();
        final Board board = Board.builder()
                .name("자유 게시판")
                .build();
        final Post post = Post.builder()
                .board(board)
                .writer(user)
                .content("content")
                .title("title")
                .isAnonymous(true)
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
}
