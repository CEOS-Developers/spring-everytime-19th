package com.ceos19.springeverytime.postlike.presentation;

import com.ceos19.springeverytime.postlike.dto.PostLikeDto;
import com.ceos19.springeverytime.postlike.service.PostLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "PostLike Controller", description = "좋아요 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/api/post_like")
public class PostLikeController {
    private final PostLikeService postLikeService;

    @GetMapping("/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시물의 좋아요 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시물의 좋아요가 존재하지 않음")
    })
    @Operation(summary = "게시물 좋아요 리스트 조회", description="게시물의 좋아요 리스트를 조회")
    public ResponseEntity<List<PostLikeDto>> postLikeListByPost(@PathVariable Long postId) {
        List<PostLikeDto> result = postLikeService.getAllLikeByPost(postId).stream()
                .map(PostLikeDto::of)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{postId}/{userId}")
    @Operation(summary = "게시물에 누른 유저의 좋아요 조회", description = "특정 게시물의 누른 특정 유저의 좋아요를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 조회 성공"),
            @ApiResponse(responseCode = "404", description = "조회하려는 특정 게시물의 특정 유저의 좋아요가 존재하지 않음")
    })
    public ResponseEntity<PostLikeDto> postLikeDetail(@PathVariable Long postId, @PathVariable Long userId) throws Exception{
        return ResponseEntity.ok(postLikeService.getLikeByPostAndUser(postId, userId));
    }

    @PostMapping("")
    @Operation(summary = "좋아요 생성", description = "게시물에 새로운 좋아요를 생성해 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 생성 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시물")
    })
    public ResponseEntity<Void> postLikeAdd(PostLikeDto postLikeDto){
        postLikeService.createLike(postLikeDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    @Operation(summary = "좋아요 삭제", description = "생성된 좋아요를 삭제")
    public ResponseEntity<Void> postLikeDelete(PostLikeDto postLikeDto){
        postLikeService.deleteLike(postLikeDto);
        return ResponseEntity.ok().build();
    }



}
