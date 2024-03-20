package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.domain.like.PostLike;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    private User user1, user2;
    private Category category;

    @BeforeEach
    public void 테스트_환경_세팅() {
        user1 = createUser("user1");
        user2 = createUser("user2");
        category = createCategory(user1);
    }

    @Test
    public void 좋아요_생성_테스트() throws Exception {
        //given
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), user1, category);
        postRepository.save(post1);

        //when
        PostLike like1 = new PostLike(user1, new Date(), post1);
        PostLike like2 = new PostLike(user2, new Date(), post1);
        likeRepository.save(like1);
        likeRepository.save(like2);

        em.flush();
        em.clear();

        //then
        Assertions.assertEquals(2, em.find(Post.class, post1.getPostId()).getPostLikes().size());
    }

    private User createUser(String id) {
        User user = new User(
                id,
                "1234",
                "nickname",
                "kim",
                "computer",
                "20",
                "test@example.com",
                true,
                new Date());

        userRepository.save(user);
        return user;
    }

    private Category createCategory(User manager) {
        Category category = new Category("자유게시판", "자유롭게 이야기 해봐요", manager);
        categoryRepository.save(category);
        return category;
    }
}
