package com.ceos19.springeverytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.domain.Post;
import jakarta.persistence.EntityNotFoundException;
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
        Post post = new Post();

        //when
        postRepository.save(post);
        Post result = postRepository.findOne(post.getId()).orElseThrow(EntityNotFoundException::new);

        //then
        assertEquals(post, result);
    }

    @Test
    void findAll() {
        //given
        Post post1 = new Post();
        Post post2 = new Post();
        Post post3 = new Post();
        Post post4 = new Post();

        //when
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

        List<Post> allPosts = postRepository.findAll();

        //then

        assertEquals(4,allPosts.size());
    }

    @Test
    void delete() {
        //given
        Post post = new Post();
        //when
        postRepository.save(post);
        postRepository.delete(post);

        //then
        assertThrows(EntityNotFoundException.class, () -> postRepository.findOne(post.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found")));
    }
}