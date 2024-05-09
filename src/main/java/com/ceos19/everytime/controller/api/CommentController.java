package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.dto.AddCommentRequest;
import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.dto.ModifyCommentRequest;
import com.ceos19.everytime.dto.ReadCommentResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{comment_id}/reply")
    public ResponseEntity<BaseResponse> addReply(@PathVariable("comment_id") Long commentId, @Valid @RequestBody AddCommentRequest request) {
        try {
            Long id = commentService.addReply(request, commentId);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/{comment_id}/replies")
    public ResponseEntity<BaseResponse<List<ReadCommentResponse>>> readReply(@PathVariable("comment_id") Long commentId) {
        try {
            List<ReadCommentResponse> value = new ArrayList<>();
            commentService.findCommentReplies(commentId).
                    forEach(reply -> {
                        value.add(ReadCommentResponse.from(reply));
                    });

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @PutMapping("/{comment_id}")
    public ResponseEntity<BaseResponse> modifyComment(@PathVariable("comment_id") Long commentId, @Valid @RequestBody ModifyCommentRequest request) {
        try {
            commentService.modifyComment(commentId, request.getContent());
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<BaseResponse> deleteComment(@PathVariable("comment_id") Long commentId) {
        try {
            commentService.removeComment(commentId);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }


}
