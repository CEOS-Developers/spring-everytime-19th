package com.ceos19.springeverytime.domain.post.controller;

import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.post.dto.request.PostCreateRequest;
import com.ceos19.springeverytime.domain.post.dto.response.PostDetailResponse;
import com.ceos19.springeverytime.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category/{categoryId}/post")
public class PostController {
    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
        PostDetailResponse response = postService.getPostDetail(postId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<Void> createPost(
            @PathVariable Long categoryId,
            @RequestBody @Valid final PostCreateRequest request
    ) {
        final Post post = postService.save(categoryId, request);
        return ResponseEntity.created(URI.create("/category/"+categoryId+"/post/"+post.getPostId())).build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}
