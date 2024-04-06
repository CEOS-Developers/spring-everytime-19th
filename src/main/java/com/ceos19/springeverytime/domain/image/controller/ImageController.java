package com.ceos19.springeverytime.domain.image.controller;

import com.ceos19.springeverytime.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;
    @PostMapping
    public ResponseEntity<Void> uploadImage(@RequestPart final MultipartFile image) {
        imageService.upload(image);
        return ResponseEntity.ok().build();
    }
}
