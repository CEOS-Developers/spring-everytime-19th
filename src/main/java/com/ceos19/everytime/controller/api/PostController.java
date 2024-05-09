package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.*;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
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
            Long id = commentService.addComment(request, postId);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("{post_id}/comments")
    public ResponseEntity<BaseResponse<List<ReadCommentResponse>>> readComments(@PathVariable("post_id") Long postId) {
        try {
            List<ReadCommentResponse> value = new ArrayList<>();
            commentService.findCommentByPostId(postId).forEach(comment -> {
                value.add(ReadCommentResponse.from(comment));
            });

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/{post_id}/postLike")
    public ResponseEntity<BaseResponse<List<ReadPostLikeResponse>>> readPostLike(@PathVariable("post_id") Long postId) {
        try {
            List<ReadPostLikeResponse> value = new ArrayList<>();
            postLikeService.findPostLikeByPostId(postId).forEach(postLike -> {
                value.add(ReadPostLikeResponse.from(postLike));
            });
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/{post_id}/attachments")
    public ResponseEntity<BaseResponse<List<ReadAttachmentResponse>>> readAttachments(@PathVariable("post_id") Long postId) {
        try {
            List<ReadAttachmentResponse> value = new ArrayList<>();
            postService.findPostById(postId).getAttachments().forEach(attachment -> {
                value.add(ReadAttachmentResponse.from(attachment));
            });
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @PutMapping("/{post_id}")
    public ResponseEntity<BaseResponse> modifyPost(@PathVariable("post_id") Long postId, @Valid @RequestBody ModifyPostRequest request) {
        try {
            postService.modifyPost(postId, request);
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @PostMapping("/post/{post_id}/attachment")
    public ResponseEntity<BaseResponse> addAttachment(@PathVariable("post_id") Long postId, @RequestBody AddAttachmentRequest request) {
        try {
            Long id = postService.addAttachment(postId, request);

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<BaseResponse> deletePost(@PathVariable("post_id") Long postId) {
        try {
            postService.removePost(postId);
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
}
