package com.ceos19.everytime.controller;

import com.ceos19.everytime.dto.LikeRequest;
import com.ceos19.everytime.dto.LikeResponse;
import com.ceos19.everytime.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ceos19.everytime.exception.SuccessCode.INSERT_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postlike")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<LikeResponse> likeMessage (@PathVariable final Long postId, @RequestBody final LikeRequest likeRequest){

        final LikeResponse likeResponse = postLikeService.likePost(postId, likeRequest);
        return ResponseEntity.status(INSERT_SUCCESS.getHttpStatus())
                .body(likeResponse);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<LikeResponse> cancelLikeMessage (@PathVariable final Long postId, @RequestBody final LikeRequest likeRequest) {

        final LikeResponse likeResponse = postLikeService.cancelPostLike(postId, likeRequest);
        return ResponseEntity.status(INSERT_SUCCESS.getHttpStatus())
                .body(likeResponse);
    }


}
