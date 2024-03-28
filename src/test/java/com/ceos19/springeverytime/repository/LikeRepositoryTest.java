package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.category.repository.CategoryRepository;
import com.ceos19.springeverytime.domain.post.repository.PostRepository;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.like.CommentLike;
import com.ceos19.springeverytime.domain.like.PostLike;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class LikeRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    private User user1, user2;
    private Category category;

    @BeforeEach
    void 테스트_환경_세팅() {
        user1 = userRepository.save(EntityGenerator.generateUser("user1"));
        user2 = userRepository.save(EntityGenerator.generateUser("user2"));
        category = categoryRepository.save(EntityGenerator.generateCategory(user1));
    }

    @Test
    @DisplayName("게시글 좋아요 생성 테스트")
    void 게시글_좋아요_생성_테스트() throws Exception {
        //given
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, user1, category);
        postRepository.save(post1);

        //when
        PostLike like1 = post1.like(user1);
        PostLike saveLike1 = likeRepository.save(like1);

        //then
        Assertions.assertEquals(saveLike1, like1);
    }

    @Test
    @DisplayName("게시글 좋아요 취소 테스트")
    void 게시글_좋아요_취소_테스트() throws Exception {
        //given
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, user1, category);
        postRepository.save(post1);
        PostLike like1 = post1.like(user1);
        likeRepository.save(like1);
        em.flush();

        //when
        likeRepository.delete(like1);

        //then
        Optional<PostLike> findLike = likeRepository.findPostLikeByPostAndUser(post1, user1);
        Assertions.assertTrue(findLike.isEmpty());
    }

    @Test
    @DisplayName("댓글 좋아요 취소 테스트")
    void 댓글_좋아요_취소_테스트() throws Exception {
        //given
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, user1, category);
        Comment comment = new Comment("댓글1", false, user2, post1);

        postRepository.save(post1);
        commentRepository.save(comment);

        CommentLike like1 = likeRepository.save(comment.like(user1));

        //when
        likeRepository.delete(like1);

        //then
        Optional<CommentLike> findLike = likeRepository.findCommentLikeByCommentAndUser(comment, user1);
        Assertions.assertTrue(findLike.isEmpty());
    }
}
