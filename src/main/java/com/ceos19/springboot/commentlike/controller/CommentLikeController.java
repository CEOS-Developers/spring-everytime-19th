package com.ceos19.springboot.commentlike.controller;

import com.ceos19.springboot.commentlike.service.CommentLikeService;
import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("{postId}/")
    public ApiResponseDto<SuccessResponse> createCommentLike(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long postId
            )
    {
        return commentLikeService.commentLikeCreate(loginUser,postId );
    }
    @DeleteMapping("{postId}/")
    public ApiResponseDto<SuccessResponse> deleteCommentLike(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long postId
    )
    {
        return commentLikeService.commentLikeDelete(loginUser,postId );
    }

}
