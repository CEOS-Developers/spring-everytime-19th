package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.PostRepository;
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
public class PostServiceTest {
    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    User user1, user2;
    Category category;

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
    }

    @Test
    @DisplayName("포스트 생성 테스트")
    void 포스트_생성_테스트() {
        // given
        Post post = new Post(
                "송프언 과제 질문이요",
                "프롤로그 진짜 어케해요..?",
                true,
                new Date(), new Date(), user2, category);
        given(postRepository.save(any(Post.class))).willReturn(post.getPostId());

        // when
        Long newPostId = postService.save(post);

        // then
        Assertions.assertEquals(post.getPostId(), newPostId);
    }

    @Test
    @DisplayName("포스트 조회 테스트")
    void 포스트_조회_테스트() {
        // given
        Post post = new Post(
                "송프언 과제 질문이요",
                "프롤로그 진짜 어케해요..?",
                true,
                new Date(), new Date(), user2, category);
        given(postRepository.findOne(any())).willReturn(post);

        // when
        Post foundPost = postService.findOne(1L);

        // then
        Assertions.assertEquals(post, foundPost);
    }
}
