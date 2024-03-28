package com.ceos19.springeverytime.domain.category.repository;

import com.ceos19.springeverytime.domain.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}