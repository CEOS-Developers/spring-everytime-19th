package com.ceos19.everytime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.ceos19.everytime.config.JpaAuditingConfig;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import com.ceos19.everytime.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
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
