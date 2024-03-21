package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.domain.like.CommentLike;
import com.ceos19.springeverytime.domain.like.PostLike;
import com.ceos19.springeverytime.repository.LikeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {
    @Mock
    LikeRepository likeRepository;

    @InjectMocks
    LikeService likeService;

    User user1, user2;
    Category category;
    Post post;
    Comment comment;

    @BeforeEach
    void 테스트_셋업() {
        user1 = new User(
                "test",
                "adsfbsa234@ad",
                "nicnname",
                "kim",
                "computer",
                "20",
                "test@exmaple.com",
                true
        );

        user2 = new User(
                "test2",
                "adsfbsa234@ad",
                "nickname2",
                "kwon",
                "data",
                "21",
                "test2@exmaple.com",
                true
        );

        category = new Category("자유게시판", "", user1);

        post = new Post(
                "송프언 과제 질문이요",
                "프롤로그 진짜 어케해요..?",
                true,
                new Date(), new Date(), user2, category);

        new Comment("화이팅", true, user2, post);
    }

    @Test
    @DisplayName("게시글_좋아요_생성")
    void 게시글_좋아요_생성() {
        // given
        PostLike postLike = new PostLike(user1, new Date(), post);
        given(likeRepository.save(any(PostLike.class))).willReturn(postLike);

        // when
        PostLike testPostLike = likeService.createPostLike(post, user1);

        // then
        Assertions.assertEquals(postLike, testPostLike);
    }

    @Test
    @DisplayName("댓글_좋아요_생성")
    void 댓글_좋아요_생성() {
        // given
        CommentLike commentLike = new CommentLike(user1, new Date(), comment);
        given(likeRepository.save(any(CommentLike.class))).willReturn(commentLike);

        // when
        CommentLike testCommentLike = likeService.createCommentLike(comment, user1);

        // then
        Assertions.assertEquals(commentLike, testCommentLike);
    }
}
