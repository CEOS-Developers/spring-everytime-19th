package com.ceos19.springboot.post.controller;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.comment.service.CommentService;
import com.ceos19.springboot.exception.ApiResponse;
import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.post.dto.*;
import com.ceos19.springboot.post.service.PostService;
import com.ceos19.springboot.postlike.service.PostLikeService;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;
    private final UserService userService;

    /**
     * 목적 : 게시글 생성
     * 성공 : 생성된 게시글 정보 리턴
     * 실패 : 에러 메시지 출력
     */
    @PostMapping("api/post")
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(@RequestBody CombinedDto combinedDto) {
        try {
            Users user = userService.findUser(combinedDto.getUser().getUserId());
            Post createdPost = Post.builder()
                    .createdAt(LocalDateTime.now())
                    .content(combinedDto.getPost().getContent())
                    .title(combinedDto.getPost().getTitle())
                    .imageUrl(combinedDto.getPost().getImageUrl())
                    .user(user)
                    .likes(0)
                    .build();
            Long savePostId = postService.savePost(createdPost);
            PostResponseDto postResponseDto = PostResponseDto.createFromPost(createdPost, savePostId);

            ApiResponse<PostResponseDto> response = ApiResponse.of(201, "게시글 생성 성공", postResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<PostResponseDto> errorResponse = ApiResponse.of(500, e.getMessage(), null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 목적 : 게시글 조회
     * 성공 : 조회된 모든 게시글 정보 리턴
     * 실패 : 에러 메시지 출력
     */
    @GetMapping("api/post")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> retreivePost() {
        try {
            List<Post> posts = postService.retrievePost(); // 모든 게시글 조회
            List<PostResponseDto> postResponseDtos = new ArrayList<>();

            for (Post post : posts) {
                PostResponseDto postResponseDto = PostResponseDto.createFromPost(post, post.getPostId()); // 각 게시글을 PostResponseDto로 변환
                postResponseDtos.add(postResponseDto);
            }

            ApiResponse<List<PostResponseDto>> response = ApiResponse.of(200, "게시글 조회 성공", postResponseDtos);
            return ResponseEntity.ok(response); // 모든 게시글 정보를 포함하는 ApiResponse 반환
        } catch (Exception e) {
            ApiResponse<List<PostResponseDto>> errorResponse = ApiResponse.of(500, "게시글 조회 중 오류가 발생했습니다: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 목적 : 게시글 삭제
     * 성공 : "게시글 삭제 성공" 문자열 리턴
     * 실패 : 에러 메시지 출력
     */
    @DeleteMapping("api/post/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@RequestBody PostUserRequestDto postUserRequestDto,
                                                        @PathVariable("postId") Long postId) {
        Users user = userService.findUser(postUserRequestDto.getUserId());
        if (!postService.isOwner(postId, user)) {
            ApiResponse<Void> response = ApiResponse.of(401, "게시글 주인이 아닙니다", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            try {
                postService.deletePost(postId);
                ApiResponse<Void> response = ApiResponse.of(200, "게시글 삭제 성공", null);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } catch (Exception e) {
                ApiResponse<Void> errorResponse = ApiResponse.of(500, e.getMessage(), null);
                return ResponseEntity.internalServerError().body(errorResponse);
            }
        }
    }

    /**
     * 목적 : 게시글 수정
     * 성공 : 수정한 게시글 정보 리턴
     * 실패 : 에러 메시지 출력
     */
    @PatchMapping("api/post/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> patchPost(@PathVariable("postId") Long postId,
                                                                  @RequestBody PostModifyRequestDto postModifyRequestDto) {
        Users user = userService.findUser(postModifyRequestDto.getUserId());
        if (!postService.isOwner(postId, user)) {
            ApiResponse<PostResponseDto> response = ApiResponse.of(401, "게시글 주인이 아닙니다.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Post updatedPost = postService.updatePost(postId, postModifyRequestDto);
            PostResponseDto postResponseDto = PostResponseDto.updateFromPost(updatedPost, updatedPost.getPostId());
            ApiResponse<PostResponseDto> response = ApiResponse.of(200, "게시글이 성공적으로 수정되었습니다.", postResponseDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 처리 및 에러 응답
            ApiResponse<PostResponseDto> errorResponse = ApiResponse.of(500, "게시글 수정 중 오류가 발생했습니다: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 목적 : 게시글에 좋아요 누르기
     * 성공 : 좋아요 누른 게시글 정보 리턴
     * 실패 : 에러 메시지 출력
     */
    //TODO : 좋아요 중복 검사
    @PatchMapping("api/post/{postId}/like")
    public ResponseEntity<ApiResponse<PostResponseDto>> pressLikePost(@RequestBody PostUserRequestDto postUserRequestDto,
                                                                      @PathVariable("postId") Long postId) {
        try {
            Users user = userService.findUser(postUserRequestDto.getUserId());
            Post likedPost = postService.pressLike(postId);
            postLikeService.pressLikePost(user, postId);
            PostResponseDto postResponseDto = PostResponseDto.updateFromPost(likedPost, likedPost.getPostId());
            ApiResponse<PostResponseDto> response = ApiResponse.of(200, "게시글에 좋아요를 눌렀습니다.", postResponseDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<PostResponseDto> errorResponse = ApiResponse.of(500, "게시글에 좋아요 누르기 실패: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PatchMapping("api/post/{postId}/unlike")
    public ResponseEntity<ApiResponse<PostResponseDto>> pressUnlikePost(@RequestBody PostUserRequestDto postUserRequestDto,
                                                                      @PathVariable("postId") Long postId) {
        try {
            Users user = userService.findUser(postUserRequestDto.getUserId());
            Post unlikePost = postService.findPost(postId);
            postService.unLike(unlikePost);

            PostResponseDto postResponseDto = PostResponseDto.updateFromPost(unlikePost, unlikePost.getPostId());
            ApiResponse<PostResponseDto> response = ApiResponse.of(200, "게시글에 좋아요를 취소했습니다.", postResponseDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<PostResponseDto> errorResponse = ApiResponse.of(500, "게시글에 좋아요 취소하기 실패: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("api/post/{postId}")
    public ResponseEntity<ApiResponse<List<PostCommentResponseDto>>> retreiveOnePost(@PathVariable("postId") Long postId) {
        try {
            Post retreivedPost = postService.retreiveOnePost(postId);
            List<Comment> retreivedComment = commentService.retreiveComment(postId);
            List<PostCommentResponseDto> postCommentResponseDtos = new ArrayList<>();
            if (retreivedComment.isEmpty()) { // 댓글이 없는 경우
                PostCommentResponseDto postCommentResponseDto = PostCommentResponseDto.createIfNoCommentFromPost(retreivedPost);
                postCommentResponseDtos.add(postCommentResponseDto);
                ApiResponse<List<PostCommentResponseDto>> response = ApiResponse.of(200, "게시글 조회 성공", postCommentResponseDtos);
                return ResponseEntity.ok(response);
            } else {
                for (Comment comment : retreivedComment) {
                    PostCommentResponseDto postCommentResponseDto = PostCommentResponseDto.createFromPost(retreivedPost,comment); // 각 게시글을 PostResponseDto로 변환
                    postCommentResponseDtos.add(postCommentResponseDto);
                }
                ApiResponse<List<PostCommentResponseDto>> response = ApiResponse.of(200, "게시글 조회 성공", postCommentResponseDtos);
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            ApiResponse<List<PostCommentResponseDto>> errorResponse = ApiResponse.of(500, "게시글 조회 실패 : " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}




