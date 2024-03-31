package com.ceos19.springeverytime.comment.presentation;

import com.ceos19.springeverytime.comment.dto.CommentDto;
import com.ceos19.springeverytime.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/{postId}")
@Tag(name = "Comment Controller", description = "댓글 컨트롤러")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "게시물의 댓글 조회", description = "게시물의 모든 댓글을 조회")
    public ResponseEntity<List<CommentDto>> commentListByPost(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @PostMapping
    @Operation(summary = "게시물에 댓글 생성", description = "게시물에 새로운 댓글을 생성")
    public ResponseEntity<Void> commentAdd(@RequestBody CommentDto request)
            throws NotFoundException {
        commentService.createComment(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제")
    public ResponseEntity<Void> commentDelete(@RequestBody CommentDto request){
       commentService.deleteComment(request.getId());
       return ResponseEntity.ok().build();
    }
}
