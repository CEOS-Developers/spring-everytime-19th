package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Image;
import com.ceos19.springeverytime.domain.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageRepository {
    private final EntityManager em;

    public List<Image> findAllByPost(Post post) {
        return em.createQuery("select i from Image i where i.post = :post",Image.class)
                .setParameter("post", post)
                .getResultList();
    }
}
