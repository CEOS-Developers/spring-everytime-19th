package com.ceos19.springeverytime.domain.like.controller;

import com.ceos19.springeverytime.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class likeController {
    private final LikeService likeService;

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<Void> updatePostLike(@PathVariable final Long postId) {
        // TEMPORARY
        Long userId = 1L;
        // TEMPORARY

        // redis를 사용해서 캐시로 구현한 예제가 있어, 나중에 리펙토링 해보고 싶다.
        likeService.updatePostLike(postId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> updateCommentLike(@PathVariable final Long commentId) {
        // TEMPORARY
        Long userId = 1L;
        // TEMPORARY

        // redis를 사용해서 캐시로 구현한 예제가 있어, 나중에 리펙토링 해보고 싶다.
        likeService.updateCommentLike(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
