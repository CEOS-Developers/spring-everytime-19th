package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Image;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class ImageRepositoryTest {
    @Autowired ImageRepository imageRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;

    User user;
    Category category;
    Post post;

    @BeforeEach
    void 테스트_셋업() {
        user = createUser("id1");
        category = createCategory(user);
        post = createPost(user, category);
    }

    @Test
    @DisplayName("이미지 등록 테스트")
    void 이미지_등록_테스트() {
        // given
        Image image = new Image("test/img1.png", post);

        // when
        Image saveImage = imageRepository.save(image);

        // then
        Assertions.assertEquals(saveImage, image);
        Assertions.assertEquals(saveImage.getImageUrl(), "test/img1.png");
    }

    @Test
    @DisplayName("이미지 삭제 테스트")
    void 이미지_삭제_테스트() {
        // given
        Image image = new Image("test/img1.png", post);
        imageRepository.save(image);

        // when
        imageRepository.delete(image);

        // then
        List<Image> imagesOfPost = imageRepository.findAllByPost(post);
        Assertions.assertEquals(imagesOfPost.size(), 0);
        Assertions.assertTrue(imageRepository.findById(image.getImageId()).isEmpty());
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

    private Post createPost(User author, Category category) {
        Post post = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), author, category);
        return postRepository.save(post);

    }
}
