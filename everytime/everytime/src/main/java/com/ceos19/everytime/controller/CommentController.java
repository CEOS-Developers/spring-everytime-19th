package com.ceos19.everytime.controller;

import com.ceos19.everytime.domain.AboutPost.Comment;
import com.ceos19.everytime.dto.CommentDTO;
import com.ceos19.everytime.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/post")
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //1. comment 작성
    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> postComment(@Valid @PathVariable long postId, @RequestBody CommentDTO commentDTO) {

        Comment comment  = commentService.addNewComment(commentDTO, postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //2.comment 리스트 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@Valid @PathVariable long postId) {
        List<CommentDTO> commentList = commentService.getComments(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
