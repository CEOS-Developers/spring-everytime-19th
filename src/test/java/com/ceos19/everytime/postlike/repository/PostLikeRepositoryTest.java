package com.ceos19.everytime.postlike.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.post.repository.PostRepository;
import com.ceos19.everytime.postlike.domain.PostLike;
import com.ceos19.everytime.helper.RepositoryTest;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.user.repository.UserRepository;

@RepositoryTest
class PostLikeRepositoryTest {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("test")
                .password("qwe123!")
                .nickname("nick")
                .build();
        post = Post.builder()
                .title("title")
                .writer(user)
                .content("content")
                .build();
        userRepository.save(user);
        postRepository.save(post);
    }

    @Test
    void 좋아요를_이미_눌렀다면_true를_반환한다() {
        // given
        final PostLike postLike = new PostLike(user, post);
        postLikeRepository.save(postLike);

        // when
        final boolean result = postLikeRepository.existsByPostAndUser(post, user);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 좋아요를_누르지_않았다면_false를_반환한다() {
        final boolean result = postLikeRepository.existsByPostAndUser(post, user);

        assertThat(result).isFalse();
    }
}
