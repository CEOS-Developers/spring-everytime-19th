package com.ceos19.everytime.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.ceos19.everytime.config.JpaAuditingConfig;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class PostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private Post post1;
    private Post post2;
    private Post post3;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("test")
                .password("qwe123!")
                .nickname("nick")
                .build();
        post1 = Post.builder()
                .title("title1")
                .writer(user)
                .content("content1")
                .build();
        post2 = Post.builder()
                .title("title1")
                .writer(user)
                .content("content1")
                .build();
        post3 = Post.builder()
                .title("title1")
                .writer(user)
                .content("content1")
                .build();
        userRepository.save(user);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }

    @Test
    void 게시글_조회_테스트() {
        // given & when
        final Post post1 = postRepository.findById(1L).get();
        final Post post2 = postRepository.findById(2L).get();
        final Post post3 = postRepository.findById(3L).get();

        // then
        assertAll(
                () -> assertThat(post1).usingRecursiveComparison().isEqualTo(this.post1),
                () -> assertThat(post2).usingRecursiveComparison().isEqualTo(this.post2),
                () -> assertThat(post3).usingRecursiveComparison().isEqualTo(this.post3)
        );
    }
}
