package com.ceos19.springboot.post.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostRequestDto {
    private String title;
    private String content;
    private String imageUrl;
}
