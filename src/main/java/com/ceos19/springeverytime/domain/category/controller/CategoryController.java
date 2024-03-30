package com.ceos19.springeverytime.domain.category.controller;

import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.user.service.UserService;
import com.ceos19.springeverytime.domain.category.dto.request.CategoryCreateRequest;
import com.ceos19.springeverytime.domain.category.dto.request.CategoryUpdateRequest;
import com.ceos19.springeverytime.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody @Valid final CategoryCreateRequest request) {
        // test user
        User user = userService.register(new User(
            "test",
            "1234",
            "nickname",
            "kim",
            "computer",
            "20",
            "test@example.com"
        ));

        Category category =  categoryService.createCategory(user.getUserId(), request);
        return ResponseEntity.created(URI.create("/category/" + category.getCategoryId())).build();
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable final Long category_id,
            @RequestBody @Valid final CategoryUpdateRequest request
    ) {
        // 글 작성자인지 검증하는 로직이 필요하다.
        categoryService.updateDescription(category_id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable final Long category_id) {
        // 글 작성자인지 검증하는 로직이 필요하다.
        categoryService.delete(category_id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{category_id}/page/{page_number}")
    public ResponseEntity<List<Post>> getPostsOfPageFromCategory(
            @PathVariable final Long category_id,
            @PathVariable final int page_number
    ) {
        List<Post> posts = categoryService.getPosts(category_id, page_number);
        return ResponseEntity.ok(posts);
    }
}
