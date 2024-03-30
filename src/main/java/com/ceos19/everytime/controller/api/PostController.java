package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import com.ceos19.everytime.dto.AddPostLikeRequest;
import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.dto.ReadPostResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.CommentService;
import com.ceos19.everytime.service.PostLikeService;
import com.ceos19.everytime.service.PostService;
import com.ceos19.everytime.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/{pid}")
    public BaseResponse<ReadPostResponse> readPost(@PathVariable("pid") Long postId) {
        try {
            Post post = postService.findPostById(postId);
            ReadPostResponse value = ReadPostResponse.from(post);
            return new BaseResponse<>(HttpStatus.OK, null, value, 1);
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @PostMapping("/{pid}/postLikes")
    public BaseResponse addPostLike(@PathVariable("pid") Long postId, @Valid @RequestBody AddPostLikeRequest request) {
        try {
            Long id = postLikeService.addPostLike(postId, request.getUserId());
            return new BaseResponse(HttpStatus.OK, null, id, 1);
        } catch (AppException e) {
            return new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }
}
