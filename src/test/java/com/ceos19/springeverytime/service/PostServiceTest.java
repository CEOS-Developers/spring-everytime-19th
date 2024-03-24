package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.common.EntityGenerator;
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
import java.util.Optional;

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
        user1 = EntityGenerator.generateUser("test1");
        user2 = EntityGenerator.generateUser("test2");
        category = EntityGenerator.generateCategory(user1);
    }

    @Test
    @DisplayName("포스트 생성 테스트")
    void 포스트_생성_테스트() {
        // given
        Post post = EntityGenerator.generatePost(user1, category);
        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        Post newPost = postService.save(post);

        // then
        Assertions.assertEquals(post, newPost);
    }

    @Test
    @DisplayName("포스트 조회 테스트")
    void 포스트_조회_테스트() {
        // given
        Post post = EntityGenerator.generatePost(user2, category);
        given(postRepository.findById(any())).willReturn(Optional.of(post));

        // when
        Post foundPost = postService.findById(1L);

        // then
        Assertions.assertEquals(post, foundPost);
    }
}
