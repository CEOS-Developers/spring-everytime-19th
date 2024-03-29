package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.comment.dto.request.CommentCreateRequest;
import com.ceos19.springeverytime.domain.comment.service.CommentService;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.post.repository.PostRepository;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.comment.repository.CommentRepository;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import com.ceos19.springeverytime.global.exception.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;

    @InjectMocks
    CommentService commentService;

    User user1, user2;
    Category category;
    Post post;

    @BeforeEach
    void 테스트_셋업() {
        user1 = EntityGenerator.generateUser("test1");
        user2 = EntityGenerator.generateUser("test2");
        category = EntityGenerator.generateCategory(user1);
        post = EntityGenerator.generatePost(user1, category);
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void 댓글_작성_테스트() {
        // given
        Comment comment = new Comment("화이팅", true, user2, post);
        CommentCreateRequest request = new CommentCreateRequest("화이팅!", true);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user2));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        Comment comment1 = commentService.save(1L, 1L, request);

        // then
        Assertions.assertThat(comment1)
                .usingRecursiveComparison().isEqualTo(comment);
    }

    @Test
    @DisplayName("댓글을 삭제한다.")
    void 댓글_삭제_테스트() {
        // given & when
        commentService.deleteComment(1L);

        // then
        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("주어진 유저가 댓글의 작성자인지 검증한다.")
    void 댓글_작성자_검증_테스트() {
        // given
        given(commentRepository.existsByAuthorUserIdAndCommentId(anyLong(), anyLong()))
                .willReturn(false);

        // when & then
        Assertions.assertThatThrownBy(()->{commentService.validateCommentByUser(1L, 1L);})
                .isInstanceOf(BadRequestException.class);
    }
}
