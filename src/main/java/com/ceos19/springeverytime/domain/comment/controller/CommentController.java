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
            @PathVariable Long postId,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        // TEMPORARY
        Long userId = 1L;
        // TEMPORARY

        Long commentId = commentService.save(postId, userId, request).getCommentId();
        return ResponseEntity.created(URI.create("/comments/"+commentId)).build();
    }
}
