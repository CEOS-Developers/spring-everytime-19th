package com.ceos19.springeverytime.domain.post.controller;

import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.post.dto.request.PostCreateRequest;
import com.ceos19.springeverytime.domain.post.dto.response.PostDetailResponse;
import com.ceos19.springeverytime.domain.post.service.PostService;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
        PostDetailResponse response = postService.getPostDetail(postId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<Void> createPost(
            @RequestBody @Valid final PostCreateRequest request
    ) {
        /// TEMPORARY
        final Long userId = 1L;
        /// TEMPORARY
        final Post post = postService.save(userId, request);
        return ResponseEntity.created(URI.create("/post/"+post.getPostId())).build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        /// TEMPORARY
        final Long userId = 1L;
        /// TEMPORARY

        postService.validatePostByUser(userId, postId);
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}
