package com.ceos19.springeverytime.post.dto;

import com.ceos19.springeverytime.post.domain.Post;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePostDto {
    private Long id;
    private String title;
    private Long categoryId;

    public static ResponsePostDto of (Post post) {
        return ResponsePostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .categoryId(post.getCategory().getId())
                .build();
    }
}
