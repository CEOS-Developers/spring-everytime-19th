package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.CategoryRepository;
import com.ceos19.springeverytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Category findOne(Long categoryId) {
        return categoryRepository.findOne(categoryId);
    }

    @Transactional
    public Long create(Category category) {
        categoryRepository.save(category);
        return category.getCategoryId();
    }

    @Transactional
    public void changeManager(Long categoryId, Long newManagerId) {
        Category category = categoryRepository.findOne(categoryId);
        User newManager = userRepository.findOne(newManagerId);
        category.changeManager(newManager);
    }

    public List<Post> getPosts(Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        return category.getPosts();
    }
}
