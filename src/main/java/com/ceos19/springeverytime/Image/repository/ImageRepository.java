package com.ceos19.springeverytime.Image.repository;

import com.ceos19.springeverytime.Image.domain.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long id);

    @Query("select i from Image i where i.id in : ids")
    List<Image> findImagesByImageIds(@Param("ids") List<Long> ids);
}
