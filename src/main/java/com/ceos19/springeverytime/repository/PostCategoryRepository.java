package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.PostCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    Optional<PostCategory> findByName(String name);
}