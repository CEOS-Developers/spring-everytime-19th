package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class PostRepositoryTests {
    @Autowired PostRepository postRepository;
    @Autowired UserRepository userRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired CategoryRepository categoryRepository;

    User user;
    Category category;

    @BeforeEach
    void 테스트_셋업() {
        user = userRepository.save(EntityGenerator.generateUser("user1"));
        category = categoryRepository.save(EntityGenerator.generateCategory(user));
    }

    @Test
    public void 게시글_생성_테스트() throws Exception {
        //given
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), user, category);

        //when
        postRepository.save(post1);

        //then
        Assertions.assertEquals(postRepository.findById(post1.getPostId()).get(), post1);
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void 게시글_수정_테스트() throws Exception {
        //given
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), user, category);
        postRepository.save(post1);

        //when
        post1.modify("첫번째 글 수정", "첫번째 글 수정입니다.");

        //then
        Post testPost = postRepository.findById(post1.getPostId()).get();
        Assertions.assertEquals(testPost, post1);
        Assertions.assertEquals(testPost.getTitle(), "첫번째 글 수정");
        Assertions.assertEquals(testPost.getContent(), "첫번째 글 수정입니다.");
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void 게시글_삭제_테스트() throws Exception {
        //given
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), user, category);
        postRepository.save(post1);

        //when
        postRepository.delete(post1);

        //then
        Optional<Post> testPost = postRepository.findById(post1.getPostId());
        Assertions.assertTrue(testPost.isEmpty());
    }

    @Test
    @DisplayName("게시글 & 댓글 동시 삭제 테스트")
    public void 게시글과_댓글_삭제_테스트() throws Exception {
        //given
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), user, category);
        postRepository.save(post1);
        post1.addComment(user, "test1", true);
        post1.addComment(user, "test2", true);
        post1.addComment(user, "test3", true);

        //when
        postRepository.delete(post1);

        //then
        Optional<Post> testPost = postRepository.findById(post1.getPostId());
        List<Comment> comments = commentRepository.findAllByPost(post1);
        Assertions.assertTrue(testPost.isEmpty());
        Assertions.assertTrue(comments.isEmpty());
    }
}
