package com.ceos19.springeverytime.post.presentation;

import com.ceos19.springeverytime.post.dto.RequestPostDto;
import com.ceos19.springeverytime.post.dto.ResponsePostDto;
import com.ceos19.springeverytime.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Post Controller", description = "게시물 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/post")
public class PostController {
    private final PostService postService;

    @GetMapping
    @Operation(summary = "제목으로 게시물 조회", description = "검색한 제목이 포함된 게시물들을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시물 조회 실패")
    })
    public ResponseEntity<List<ResponsePostDto>> postDetail(@RequestBody String title) throws NotFoundException {
        return ResponseEntity.ok(postService.getPostByTitle(title));
    }

    @PostMapping
    @Operation(summary = "게시물 생성", description = "게시물을 새롭게 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시물 생성 성공"),
            @ApiResponse(responseCode = "400", description = "1. 잘못된 게시물 양식"
                    + "2. 올바르지 않은 이미지 파일")})
    public ResponseEntity<Void> postAdd(@RequestBody RequestPostDto requestPostDto){
        postService.createPost(requestPostDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "게시물 삭제", description = "존재하는 게시물을 삭제")
    public ResponseEntity<Void> postDelete(@RequestBody Long postId){
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    @Operation(summary = "게시물 수정", description = "게시물의 내용을 수정")
    public ResponseEntity<Void> postUpdate(@RequestBody RequestPostDto request) throws Exception {
        postService.updatePost(request);
        return ResponseEntity.ok().build();
    }
}
