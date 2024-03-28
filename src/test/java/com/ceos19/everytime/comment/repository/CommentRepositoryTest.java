package com.ceos19.everytime.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceos19.everytime.comment.domain.Comment;
import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.helper.RepositoryTest;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.post.repository.PostRepository;
import com.ceos19.everytime.user.repository.UserRepository;

@RepositoryTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager em;

    @Test
    void 댓글을_삭제한다() {
        // given
        final User user = User.builder()
                .nickname("nickname")
                .password("password")
                .username("username")
                .build();
        userRepository.save(user);

        final Post post = Post.builder()
                .title("test")
                .content("content")
                .isAnonymous(false)
                .writer(user)
                .build();
        postRepository.save(post);

        final Comment comment = Comment.builder()
                .content("content")
                .isAnonymous(false)
                .user(user)
                .post(post)
                .build();
        commentRepository.save(comment);

        // when
        commentRepository.deleteById(1L);

        // then
        final Boolean result = (Boolean) em.createNativeQuery("SELECT is_deleted FROM comment WHERE comment_id = 1",
                        Boolean.class)
                .getSingleResult();
        assertThat(result).isTrue();
    }
}
