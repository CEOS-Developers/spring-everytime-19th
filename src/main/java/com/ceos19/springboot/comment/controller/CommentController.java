package com.ceos19.springboot.comment.controller;

import com.ceos19.springboot.comment.dto.CommentRequestDto;
import com.ceos19.springboot.comment.service.CommentService;
import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ApiResponseDto<SuccessResponse> createComment(
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable Long postId
            )
    {
        return commentService.createComment(loginUser, commentRequestDto, postId);
    }
    @DeleteMapping("/{commentId}")
    public ApiResponseDto<SuccessResponse> deleteComment(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long commentId
    )
    {
        return commentService.deleteComment(loginUser,commentId);
    }
}
