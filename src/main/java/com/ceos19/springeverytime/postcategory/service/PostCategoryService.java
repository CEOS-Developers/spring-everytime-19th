package com.ceos19.springeverytime.postcategory.service;

import com.ceos19.springeverytime.postcategory.domain.PostCategory;
import com.ceos19.springeverytime.postcategory.repository.PostCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostCategoryService {
    private final PostCategoryRepository postCategoryRepository;

    @Transactional
    public void createCategory(String categoryName){
        if(postCategoryRepository.existsPostCategoryByName(categoryName))
        {
            throw new IllegalStateException("category already exists");
        }

        PostCategory newCategory = PostCategory.builder()
                .name(categoryName)
                .build();

        postCategoryRepository.save(newCategory);
    }

    public PostCategory getPostCategoryByCategoryId(Long categoryId){
        return postCategoryRepository.findById(categoryId)
                .orElseThrow(IllegalStateException::new);
    }

    public void deletePostCategory(Long categoryId){
        postCategoryRepository.deleteById(categoryId);
    }

    public List<PostCategory> getAllPostCategories() {
        return postCategoryRepository.findAll();
    }
}
