package com.ceos19.springboot.controller;

import com.ceos19.springboot.domain.Post;
import com.ceos19.springboot.dto.PostResponseDTO;
import com.ceos19.springboot.exception.ErrorException;
import com.ceos19.springboot.service.PostService;
import com.ceos19.springboot.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @PostMapping("/{pid}/upload")
    @ApiOperation(value = "upload post api", notes = "upload post with checking duplication")
    public ResponseEntity<Long> addPost(@PathVariable("pid") Long postId, Post post) {
        // 추후 form 구현 필요
        try {
            PostResponseDTO responseDto = postService.addPost(post);
            return new ResponseEntity<>(responseDto.getId(), HttpStatus.OK);
        }
        catch (ErrorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        // error exception 방법
    }
}
