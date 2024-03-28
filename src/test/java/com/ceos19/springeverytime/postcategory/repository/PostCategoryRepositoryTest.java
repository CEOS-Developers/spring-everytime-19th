package com.ceos19.springeverytime.postcategory.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.postcategory.domain.PostCategory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class PostCategoryRepositoryTest {
    @Autowired
    PostCategoryRepository postCategoryRepository;

    @Test
    void findByName() {
        //given
        PostCategory postCategory = PostCategory.builder()
                .name("test")
                .build();
        //when
        postCategoryRepository.save(postCategory);

        //then
        PostCategory result = postCategoryRepository.findByName("test").orElseThrow(IllegalStateException::new);
        assertEquals(postCategory, result);
    }
}