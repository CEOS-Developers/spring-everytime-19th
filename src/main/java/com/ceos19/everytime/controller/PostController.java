package com.ceos19.everytime.controller;

import com.ceos19.everytime.dto.*;
import com.ceos19.everytime.dto.Post.CreatePostRequest;
import com.ceos19.everytime.dto.Post.PostResponse;
import com.ceos19.everytime.dto.Post.PostUpdateRequest;
import com.ceos19.everytime.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ceos19.everytime.exception.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping()
    public ResponseEntity<CreateResponse> createPost (@RequestBody @Valid final CreatePostRequest createPostRequest) {
        final Long postId = postService.publish(createPostRequest);
        return ResponseEntity.status(INSERT_SUCCESS.getHttpStatus())
                .body(CreateResponse.from(postId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findPost (@PathVariable final Long postId){
        final PostResponse postResponse = postService.findPost(postId);
        return ResponseEntity.status(SELECT_SUCCESS.getHttpStatus())
                .body(postResponse);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<PostResponse>> findEveryPost (@PathVariable final Long boardId){
        final List<PostResponse> postResponse = postService.findEveryPost(boardId);
        return ResponseEntity.status(SELECT_SUCCESS.getHttpStatus())
                .body(postResponse);
    }

    @PutMapping("{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable final Long postId, @RequestBody @Valid final PostUpdateRequest postUpdateRequest){
        postService.updatePost(postId, postUpdateRequest);
        return ResponseEntity.status(UPDATE_SUCCESS.getHttpStatus()).build();
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable final Long boardId, @RequestBody @Valid final DeleteRequest deleteRequest) {
        postService.delete(boardId, deleteRequest);
        return ResponseEntity.status(DELETE_SUCCESS.getHttpStatus()).build();
    }

}
