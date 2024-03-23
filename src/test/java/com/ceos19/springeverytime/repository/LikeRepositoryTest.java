package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.domain.like.CommentLike;
import com.ceos19.springeverytime.domain.like.Like;
import com.ceos19.springeverytime.domain.like.PostLike;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
        user1 = createUser("user1");
        user2 = createUser("user2");
        category = createCategory(user1);
    }

    @Test
    @DisplayName("게시글 좋아요 생성 테스트")
    void 게시글_좋아요_생성_테스트() throws Exception {
        //given
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), user1, category);
        postRepository.save(post1);

        //when
        PostLike like1 = new PostLike(user1, new Date(), post1);
        PostLike saveLike1 = likeRepository.save(like1);

        //then
        Assertions.assertEquals(saveLike1, like1);
    }

    @Test
    @DisplayName("게시글 좋아요 취소 테스트")
    void 게시글_좋아요_취소_테스트() throws Exception {
        //given
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), user1, category);
        postRepository.save(post1);
        PostLike like1 = likeRepository.save(new PostLike(user1, new Date(), post1));

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
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), user1, category);
        Comment comment = new Comment("댓글1", false, user2, post1);

        postRepository.save(post1);
        commentRepository.save(comment);

        CommentLike like1 = likeRepository.save(new CommentLike(user1, new Date(), comment));

        //when
        likeRepository.delete(like1);

        //then
        Optional<CommentLike> findLike = likeRepository.findCommentLikeByCommentAndUser(comment, user1);
        Assertions.assertTrue(findLike.isEmpty());
    }

    private User createUser(String id) {
        User user = new User(
                id,
                "1234",
                "nickname",
                "kim",
                "computer",
                "20",
                "test@example.com");

        return userRepository.save(user);
    }

    private Category createCategory(User manager) {
        Category category = new Category("자유게시판", "자유롭게 이야기 해봐요", manager);
        return categoryRepository.save(category);
    }
}
