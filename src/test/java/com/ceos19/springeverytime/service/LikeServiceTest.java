package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.like.service.LikeService;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.post.repository.PostRepository;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.like.domain.CommentLike;
import com.ceos19.springeverytime.domain.like.domain.PostLike;
import com.ceos19.springeverytime.domain.like.repository.LikeRepository;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {
    @Mock
    LikeRepository likeRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;

    @InjectMocks
    LikeService likeService;

    User user1, user2;
    Category category;
    Post post;
    Comment comment;

    @BeforeEach
    void 테스트_셋업() {
        user1 = EntityGenerator.generateUser("test1");
        user2 = EntityGenerator.generateUser("test2");
        category = EntityGenerator.generateCategory(user1);
        post = EntityGenerator.generatePost(user1, category);
        comment = EntityGenerator.generateComment(user2, post);
    }

    @Test
    @DisplayName("게시글에 좋아요를 누르지 않았다면 좋아요를 생성한다.")
    void 게시글_좋아요_생성() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user1));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(likeRepository.findPostLikeByPostIdAndUserId(any(), any())).willReturn(Optional.empty());

        // when
        likeService.updatePostLike(1L, 1L);

        // then
        verify(likeRepository, times(1)).save(any(PostLike.class));
        verify(likeRepository, never()).delete(any(PostLike.class));
    }

    @Test
    @DisplayName("게시글에 좋아요를 눌렀다면 좋아요를 삭제한다.")
    void 게시글_좋아요_삭제() {
        // given
        PostLike postLike = new PostLike(user1, post);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user1));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(likeRepository.findPostLikeByPostIdAndUserId(any(), any())).willReturn(Optional.of(postLike));

        // when
        likeService.updatePostLike(1L, 1L);

        // then
        verify(likeRepository, times(1)).delete(any(PostLike.class));
        verify(likeRepository, never()).save(any(PostLike.class));
    }

    @Test
    @DisplayName("댓글_좋아요_생성")
    void 댓글_좋아요_생성() {
        // given
        CommentLike commentLike = new CommentLike(user1, comment);
        given(likeRepository.save(any(CommentLike.class))).willReturn(commentLike);

        // when
        CommentLike testCommentLike = likeService.createCommentLike(comment, user1);

        // then
        Assertions.assertEquals(commentLike, testCommentLike);
    }
}
