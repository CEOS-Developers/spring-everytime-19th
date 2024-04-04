package com.ceos19.everytime.post.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceos19.everytime.board.dto.request.BoardPostsRequestDto;
import com.ceos19.everytime.board.dto.response.BoardPostsResponseDto;
import com.ceos19.everytime.post.dto.request.PostCreateRequestDto;
import com.ceos19.everytime.post.dto.response.PostResponseDto;
import com.ceos19.everytime.post.service.PostService;
import com.ceos19.everytime.postlike.dto.request.PostLikeRequestDto;
import com.ceos19.everytime.postlike.service.PostLikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Post Controller", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
    @PostMapping
    public void createPost(@RequestBody final PostCreateRequestDto request) {
        postService.createPost(request);
    }

    @Operation(summary = "모든 게시글 조회", description = "모든 게시글을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<BoardPostsResponseDto>> getPosts(@RequestBody final BoardPostsRequestDto request) {
        final List<BoardPostsResponseDto> responses = postService.getPosts(request);
        return ResponseEntity.ok().body(responses);
    }

    @Operation(summary = "게시글 조회", description = "게시글을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable final Long postId) {
        final PostResponseDto response = postService.getPost(postId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "게시글 좋아요 등록", description = "게시글 좋아요를 등록합니다.")
    @PostMapping("/likes/{postId}")
    public void createPostLike(@PathVariable final Long postId, @RequestBody final PostLikeRequestDto request) {
        postLikeService.like(postId, request);
    }

    @Operation(summary = "게시글 좋아요 삭제", description = "게시글 좋아요를 삭제합니다.")
    @DeleteMapping("/likes/{postId}")
    public void deletePostLike(@PathVariable final Long postId, @RequestBody final PostLikeRequestDto request) {
        postLikeService.cancelLike(postId, request);
    }
}
