package com.ceos19.springeverytime.domain.comment.controller;

import com.ceos19.springeverytime.domain.comment.dto.request.CommentCreateRequest;
import com.ceos19.springeverytime.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(
            @PathVariable final Long postId,
            @RequestBody @Valid final CommentCreateRequest request
    ) {
        // TEMPORARY
        Long userId = 1L;
        // TEMPORARY

        Long commentId = commentService.save(postId, userId, request).getCommentId();
        return ResponseEntity.created(URI.create("/comments/"+commentId)).build();
    }

    @PostMapping("/{commentId}/reply")
    public ResponseEntity<Void> createReplyComment(
            @PathVariable final Long postId,
            @PathVariable final Long commentId,
            @RequestBody @Valid final CommentCreateRequest request
    ) {
        // TEMPORARY
        Long userId = 1L;
        // TEMPORARY

        Long replyCommentId = commentService.saveReply(postId, commentId, userId, request).getCommentId();
        return ResponseEntity.created(URI.create("/comments/"+commentId)).build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable final Long commentId) {
        // TEMPORARY
        Long userId = 1L;
        // TEMPORARY

        commentService.validateCommentByUser(userId, commentId);
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
