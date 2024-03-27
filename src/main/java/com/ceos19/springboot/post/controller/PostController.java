package com.ceos19.springboot.post.controller;

import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.post.dto.PostRequestDto;
import com.ceos19.springboot.post.dto.PostResponseDto;
import com.ceos19.springboot.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/")
    public ApiResponseDto<SuccessResponse> createPost(@Valid @RequestBody PostRequestDto postRequestDto,
        @AuthenticationPrincipal UserDetails loginUser)
    {
        return postService.createPost(postRequestDto, loginUser );
    }
    @GetMapping("{postId}")
    public ApiResponseDto<PostResponseDto> getOnePost(
            @AuthenticationPrincipal UserDetails loginUser, @RequestParam Long postId
            )
    {
        return postService.getOnePost(postId, loginUser);
    }
}
