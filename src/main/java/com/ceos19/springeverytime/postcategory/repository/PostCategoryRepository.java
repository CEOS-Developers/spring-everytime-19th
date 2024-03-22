package com.ceos19.springeverytime.postcategory.repository;

import com.ceos19.springeverytime.postcategory.domain.PostCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    Optional<PostCategory> findByName(String name);

    boolean existsPostCategoryByName(String name);

}