package com.ceos19.springboot.postlike.controller;

import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.post.service.PostService;
import com.ceos19.springboot.postlike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postlike")

public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ApiResponseDto<SuccessResponse> postCreate(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails loginUser
            )
    {
        return postLikeService.postLikeCreate(loginUser, postId);
    }
    @DeleteMapping("/{postId}")
    public ApiResponseDto<SuccessResponse> postDelete(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails loginUser
    )
    {
        return postLikeService.postLikeDelete(loginUser, postId);
    }
}
