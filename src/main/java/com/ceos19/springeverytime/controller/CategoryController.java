package com.ceos19.springeverytime.controller;

import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.dto.CategoryCreateRequest;
import com.ceos19.springeverytime.dto.CategoryUpdateRequest;
import com.ceos19.springeverytime.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        categoryService.createCategory(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long category_id, @RequestBody @Valid CategoryUpdateRequest request) {
        categoryService.updateCategory(category_id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long category_id) {
        categoryService.delete(category_id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{category_id}/page/{page_number}")
    public ResponseEntity<List<Post>> getPostsOfPageFromCategory(@PathVariable Long category_id, @PathVariable Long page_number) {
        List<Post> posts = categoryService.getPosts(category_id);
        return ResponseEntity.ok(posts);
    }
}
