package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.AddCommentRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/{post_id}")
    public ResponseEntity<BaseResponse<ReadPostResponse>> readPost(@PathVariable("post_id") Long postId) {
        try {
            Post post = postService.findPostById(postId);
            ReadPostResponse value = ReadPostResponse.from(post);
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @PostMapping("/{post_id}/postLike")
    public ResponseEntity<BaseResponse> addPostLike(@PathVariable("post_id") Long postId, @Valid @RequestBody AddPostLikeRequest request) {
        try {
            Long id = postLikeService.addPostLike(postId, request.getUserId());
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @PostMapping("/{post_id}/comment")
    public ResponseEntity<BaseResponse> addComment(@PathVariable("post_id") Long postId, @Valid @RequestBody AddCommentRequest request) {
        try {
            Comment comment = commentService.addComment(request, postId);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, comment.getId(), 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

}
