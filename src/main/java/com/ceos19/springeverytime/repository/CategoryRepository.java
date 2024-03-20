package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Category;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    @Autowired
    final EntityManager em;

    public Long save(Category category) {
        em.persist(category);
        return category.getCategoryId();
    }

    public Category findOne(Long categoryId) {
        return em.find(Category.class, categoryId);
    }
}
