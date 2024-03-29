package com.ceos19.springboot.comment.controller;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.comment.dto.CommentRequestDto;
import com.ceos19.springboot.comment.dto.CommentResponseDto;
import com.ceos19.springboot.comment.dto.CommentUserRequestDto;
import com.ceos19.springboot.comment.service.CommentService;
import com.ceos19.springboot.commentlike.service.CommentLikeService;
import com.ceos19.springboot.exception.ApiResponse;
import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.post.dto.PostCommentResponseDto;
import com.ceos19.springboot.post.dto.PostResponseDto;
import com.ceos19.springboot.post.service.PostService;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;
    private final CommentLikeService commentLikeService;

    //댓글 삭제
    @DeleteMapping("api/comment/{commentId}")
    public ResponseEntity<ApiResponse<Void>> patchComment(@RequestBody CommentUserRequestDto commentUserRequestDto,
                                                          @PathVariable("commentId") Long commentId) {
        Users user = userService.findUser(commentUserRequestDto.getUserId());
        if (!commentService.isOwner(commentId, user)) {
            ApiResponse<Void> response = ApiResponse.of(401, "댓글의 작성자가 아닙니다.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            try {
                commentService.deleteComment(commentId);
                ApiResponse<Void> response = ApiResponse.of(200, "댓글 삭제 성공", null);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                ApiResponse<Void> response = ApiResponse.of(500, "삭제 도중 에러가 발생했습니다 : " + e.getMessage(), null);
                return ResponseEntity.internalServerError().body(response);
            }
        }
    }

    //댓글 작성
    @PostMapping("api/comment")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        try {
            Post post = postService.findPost(commentRequestDto.getPostId());
            Comment comment = Comment.builder()
                    .content(commentRequestDto.getContent())
                    .post(post)
                    .build();
            Long saveComment = commentService.saveComment(comment);
            CommentResponseDto commentResponseDto = CommentResponseDto.createFromComment(comment);
            ApiResponse<CommentResponseDto> response = ApiResponse.of(200, "댓글 작성 완료", commentResponseDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<CommentResponseDto> response = ApiResponse.of(500, "댓글 작성중 에러가 발생했습니다. : " + e.getMessage(), null);
            return ResponseEntity.ok(response);
        }
    }

    //대댓글 작성
    @PostMapping("api/comment/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createCoComment(@RequestBody CommentRequestDto commentRequestDto,
                                                                           @PathVariable("commentId") Long commentId) {
        try {
            Comment parentComment = commentService.findComment(commentId);
            Post post = postService.findPost(commentRequestDto.getPostId());
            Comment childComment = Comment.builder()
                    .content(commentRequestDto.getContent())
                    .post(post)
                    .build();
            Long saveComment = commentService.saveComment(childComment);
            commentService.setChildComment(parentComment,childComment);
            CommentResponseDto commentResponseDto = CommentResponseDto.createFromComment(childComment);
            ApiResponse<CommentResponseDto> response = ApiResponse.of(200, "대댓글 작성 완료", commentResponseDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<CommentResponseDto> response = ApiResponse.of(500, "대댓글 작성중 에러가 발생했습니다. : " + e.getMessage(), null);
            return ResponseEntity.ok(response);
        }
    }

    @PatchMapping("api/comment/{commentId}/like")
    public ResponseEntity<ApiResponse<CommentResponseDto>> pressLikeComment(@RequestBody CommentUserRequestDto commentUserRequestDto,
                                                                            @PathVariable("commentId") Long commentId){
        try {
            Users user = userService.findUser(commentUserRequestDto.getUserId());
            Comment comment = commentService.findComment(commentId);
            commentLikeService.pressLike(comment,user);
            CommentResponseDto commentResponseDto = CommentResponseDto.createFromComment(comment);
            ApiResponse<CommentResponseDto> response = ApiResponse.of(200, "댓글에 좋아요 누르기 완료", commentResponseDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<CommentResponseDto> response = ApiResponse.of(500, "댓글에 좋아요를 누르는 중 에러가 발생했습니다. : " + e.getMessage(), null);
            return ResponseEntity.ok(response);
        }
    }

    @PatchMapping("api/comment/{commentId}/unlike")
    public ResponseEntity<ApiResponse<CommentResponseDto>> pressUnlikeComment(@RequestBody CommentUserRequestDto commentUserRequestDto,
                                                                            @PathVariable("commentId") Long commentId){
        try {
            Users user = userService.findUser(commentUserRequestDto.getUserId());
            Comment comment = commentService.findComment(commentId);
            commentLikeService.pressLike(comment,user);
            CommentResponseDto commentResponseDto = CommentResponseDto.createFromComment(comment);
            ApiResponse<CommentResponseDto> response = ApiResponse.of(200, "댓글에 좋아요 누르기 완료", commentResponseDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<CommentResponseDto> response = ApiResponse.of(500, "댓글에 좋아요를 누르는 중 에러가 발생했습니다. : " + e.getMessage(), null);
            return ResponseEntity.ok(response);
        }
    }
}
