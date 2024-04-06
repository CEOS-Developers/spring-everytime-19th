package com.ceos19.springeverytime.domain.image.repository;

import com.ceos19.springeverytime.domain.image.domain.Image;
import com.ceos19.springeverytime.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select i from Image i where i.post = :post")
    public List<Image> findAllByPost(@Param("post") Post post);
}
