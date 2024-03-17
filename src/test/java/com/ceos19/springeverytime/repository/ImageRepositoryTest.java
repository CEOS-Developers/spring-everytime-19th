package com.ceos19.springeverytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.domain.Image;
import com.ceos19.springeverytime.domain.Post;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ImageRepositoryTest {

    @Autowired private ImageRepository imageRepository;
    @Autowired private EntityManager em;
    
    @Test
    void FindOne() {
        //given
        Image image = new Image();
        //when
        imageRepository.save(image);
        //then
        Assertions.assertEquals(image, imageRepository.findOne(image.getId()));
    }

    @Test
    void findAll() {
        //given
        Image image1 = new Image();
        Image image2 = new Image();
        //when
        imageRepository.save(image1);
        imageRepository.save(image2);
        List<Image> allImages = imageRepository.findAll();
        //then
        assertEquals(2, allImages.size());
    }

    @Test
    void findByPost() {
        //given
        Post post = new Post();
        em.persist(post);
        Image image1 = new Image();
        Image image2 = new Image();
        //when
        image1.setPost(post);
        image2.setPost(post);
        imageRepository.save(image1);
        imageRepository.save(image2);
        //then
        List<Image> allImagesByPost = imageRepository.findByPost(post.getId());

        assertEquals(2,allImagesByPost.size());
    }
}