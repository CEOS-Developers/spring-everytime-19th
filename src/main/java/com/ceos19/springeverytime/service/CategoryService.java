package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.CategoryRepository;
import com.ceos19.springeverytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Category findById(Long categoryId) {
        Optional<Category> findCategory = categoryRepository.findById(categoryId);
        if (findCategory.isPresent()) return findCategory.get();
        throw new IllegalArgumentException("잘못된 카테고리 ID 입니다.");
    }

    @Transactional
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Category category) {
        validateCategoryCreatedBefore14Days(category);
        categoryRepository.delete(category);
    }

    @Transactional
    public void changeManager(Long categoryId, Long newManagerId) {
        Optional<Category> findCategory = categoryRepository.findById(categoryId);
        Optional<User> findManager = userRepository.findById(newManagerId);

        if (findCategory.isEmpty()) {
            throw new IllegalArgumentException("잘못된 카테고리 ID 입니다.");
        }

        if (findManager.isEmpty()) {
            throw new IllegalArgumentException("잘못된 User ID 입니다.");
        }

        findCategory.get().changeManager(findManager.get());
    }

    public List<Post> getPosts(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new IllegalArgumentException("잘못된 카테고리 ID 입니다.");
        }
        return category.get().getPosts();
    }

    private void validateCategoryCreatedBefore14Days(Category category) {
        LocalDateTime today = LocalDateTime.now();

        if (today.compareTo(category.getCreateDate()) < 14) {
            throw new IllegalArgumentException("게시판을 삭제하려면 생성 후 14일이 지나야 합니다.");
        }
    }
}
