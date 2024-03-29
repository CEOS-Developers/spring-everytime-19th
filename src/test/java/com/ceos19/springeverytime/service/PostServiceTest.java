package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.category.repository.CategoryRepository;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.post.dto.request.PostCreateRequest;
import com.ceos19.springeverytime.domain.post.dto.response.PostDetailResponse;
import com.ceos19.springeverytime.domain.post.service.PostService;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.post.repository.PostRepository;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    PostRepository postRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CategoryRepository categoryRepository;

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
    @DisplayName("게시글을 생성한다.")
    void 포스트_생성_테스트() {
        // given
        Post post = EntityGenerator.generatePost(user1, category);
        PostCreateRequest request = PostCreateRequest.of("제목", "내용", true);
        given(postRepository.save(any(Post.class))).willReturn(post);
        given(userRepository.findByLoginId(anyString())).willReturn(Optional.of(user1));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        Post newPost = postService.save(1L, request);

        // then
        Assertions.assertThat(newPost).usingRecursiveComparison().isEqualTo(post);
    }

    @Test
    @DisplayName("포스트 조회 테스트")
    void 포스트_조회_테스트() {
        // given
        Post post = EntityGenerator.generatePost(user2, category);
        given(postRepository.findById(any())).willReturn(Optional.of(post));

        // when
        final PostDetailResponse actual = postService.getPostDetail(1L);

        // then
        // usingRecursiveComparison 사용
        // https://ksh-coding.tistory.com/100
        Assertions.assertThat(actual).usingRecursiveComparison()
                .isEqualTo(PostDetailResponse.from(post));
    }

    @Test
    @DisplayName("게시글을 삭제한다")
    void 게시글_삭제_테스트() {
        // given
        Post post = EntityGenerator.generatePost(user2, category);
        given(postRepository.existsById(anyLong())).willReturn(true);

        // when
        postService.delete(1L);

        // then
        verify(postRepository, times(1)).deleteById(1L);
    }
}
