package com.ceos19.springeverytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.domain.Post;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired PostRepository postRepository;

    @Test
    void findOne() {
        //given
        Post post = Post.builder().build();

        //when
        postRepository.save(post);
        Post result = postRepository.findById(post.getId()).orElseThrow(IllegalStateException::new);

        //then
        assertEquals(post, result);
    }

    @Test
    void findAll() {
        //given
        Post post1 = Post.builder().build();
        Post post2 = Post.builder().build();
        Post post3 = Post.builder().build();
        Post post4 = Post.builder().build();

        //when
        postRepository.saveAll(Arrays.asList(post1, post2, post3, post4));

        List<Post> allPosts = postRepository.findAll();

        //then

        assertEquals(4,allPosts.size());
    }

    @Test
    void delete() {
        //given
        Post post = Post.builder().build();
        //when
        postRepository.save(post);
        postRepository.delete(post);

        //then
        assertThrows(EntityNotFoundException.class, () -> postRepository.findById(post.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found")));
    }
}