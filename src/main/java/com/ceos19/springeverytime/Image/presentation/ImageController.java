package com.ceos19.springeverytime.Image.presentation;

import com.ceos19.springeverytime.Image.dto.ImageDto;
import com.ceos19.springeverytime.Image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/image/{postId}")
@Tag(name = "Image Controller", description = "이미지 컨트롤러")
public class ImageController {
    private final ImageService imageService;

    @GetMapping
    @Operation(summary = "이미지 조회", description = "게시물의 이미지 조회")
    public ResponseEntity<List<ImageDto>> imageListByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(imageService.getImagesByPostId(postId));
    }

    @PostMapping
    @Operation(summary = "이미지 생성", description = "이미지를 생성합니다")
    public ResponseEntity<Void> imageAdd(@PathVariable Long postId, @RequestBody ImageDto imageDto) {
        imageService.createImage(postId, imageDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "이미지 삭제", description ="이미지를 삭제")
    public ResponseEntity<Void> imageDelete(@RequestBody ImageDto imageDto){
        imageService.deleteImage(imageDto.getId());
        return ResponseEntity.ok().build();
    }

}