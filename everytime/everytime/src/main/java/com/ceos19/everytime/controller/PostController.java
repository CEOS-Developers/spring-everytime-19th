package com.ceos19.everytime.controller;


import com.ceos19.everytime.domain.AboutPost.Post;
import com.ceos19.everytime.dto.PostDTO;
import com.ceos19.everytime.dto.PostResponseDTO;
import com.ceos19.everytime.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts")
public class PostController {

    private final PostService postService;

    //1. CREATE: 새로운 게시글 생성을 요청하는 API 만들기
    @PostMapping
    public ResponseEntity<Post> addPost(@Validated @RequestBody PostDTO postDTO) {
        Post savedPostDTO = postService.savePost(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPostDTO);
    }

    //2. READ: 모든 게시글을 조회하는 API 만들기
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> findAllPosts() {
        List<PostResponseDTO> posts = postService.findAll().stream().map(PostResponseDTO::from).toList();
        return ResponseEntity.ok().body(posts);
    }

    //3. READ: 특정 게시글만 조회하는 API 만들기
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> findPost(@PathVariable long postId){
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok().body(new PostResponseDTO(post.getTitle(), post.getContents()));
    }

    //4. DELETE: 특정 게시글을 삭제하는 API
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

}
