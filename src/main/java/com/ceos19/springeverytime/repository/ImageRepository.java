package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long id);
}
