package com.ceos19.springeverytime.postcategory.presentation;

import com.ceos19.springeverytime.postcategory.domain.PostCategory;
import com.ceos19.springeverytime.postcategory.dto.PostCategoryDto;
import com.ceos19.springeverytime.postcategory.service.PostCategoryService;
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
@RequiredArgsConstructor
@Tag(name = "PostCategory Controller", description="게시물 카테고리 컨트롤러")
@RequestMapping("/api/category")
public class PostCategoryController {
    private final PostCategoryService postCategoryService;

    @GetMapping()
    @Operation(summary = "카테고리 전체 조회", description = "존재하는 모든 카테고리 조회")
    public ResponseEntity<List<PostCategory>> categoryList(){
        return ResponseEntity.ok(postCategoryService.getAllPostCategories());
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "카테고리 단건 조회", description = "특정 카테고리를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 조회 실패")
    })
    public ResponseEntity<String> categoryDetail(@PathVariable Long categoryId){
        return ResponseEntity.ok(postCategoryService.getPostCategoryByCategoryId(categoryId).getName());
    }

    @PostMapping()
    @Operation(summary = "카테고리 생성",  description = "새로운 카테고리를 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 생성 성공"),
            @ApiResponse(responseCode = "409", description = "이미 카테고리가 존재합니다.")
    })
    public ResponseEntity<Void> categoryAdd(PostCategoryDto postCategoryDto){
        postCategoryService.createCategory(postCategoryDto.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description ="존재하는 카테고리를 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리입니다")
    })
    public ResponseEntity<Void> categoryDelete(@PathVariable Long categoryId){
        postCategoryService.deletePostCategory(categoryId);
        return ResponseEntity.ok().build();
    }
}
