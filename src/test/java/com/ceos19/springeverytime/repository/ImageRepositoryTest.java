package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.common.EntityGenerator;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
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
        user = userRepository.save(EntityGenerator.generateUser("id1"));
        category = categoryRepository.save(EntityGenerator.generateCategory(user));
        post = postRepository.save(EntityGenerator.generatePost(user, category));
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
}
