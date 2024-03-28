package com.ceos19.springeverytime.domain.category.service;

import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.category.dto.request.CategoryCreateRequest;
import com.ceos19.springeverytime.domain.category.dto.request.CategoryUpdateRequest;
import com.ceos19.springeverytime.global.exception.BadRequestException;
import com.ceos19.springeverytime.repository.CategoryRepository;
import com.ceos19.springeverytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.ceos19.springeverytime.global.exception.ExceptionCode.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CATEGORY_ID));
    }

    @Transactional
    public Category createCategory(Long userId, CategoryCreateRequest request) {
        User manager = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));

        Category category = new Category(
                request.getName(),
                request.getDescription(),
                manager
        );
        return categoryRepository.save(category);
    }

    @Transactional
    public void updateDescription(Long categoryId, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new BadRequestException(NOT_FOUND_CATEGORY_ID));

        category.updateDescription(request.getDescription());
    }

    @Transactional
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new BadRequestException(NOT_FOUND_CATEGORY_ID));

        validateCategoryCreatedBefore14Days(category);
        categoryRepository.delete(category);
    }

    @Transactional
    public void changeManager(Long categoryId, Long newManagerId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CATEGORY_ID));

        User newManager = userRepository.findById(newManagerId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));

        category.changeManager(newManager);
    }

    public List<Post> getPosts(Long categoryId, int pageNumber) {
        PageRequest request = PageRequest.of(pageNumber, 10);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CATEGORY_ID));

        return category.getPosts();
    }

    private void validateCategoryCreatedBefore14Days(Category category) {
        LocalDateTime today = LocalDateTime.now();

        if (today.compareTo(category.getCreateDate()) < 14) {
            throw new BadRequestException(SHOULD_EXCEED_14_DAYS_TO_DELETE);
        }
    }
}
