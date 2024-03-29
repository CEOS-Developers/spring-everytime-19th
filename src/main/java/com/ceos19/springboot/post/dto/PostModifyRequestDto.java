package com.ceos19.springboot.post.dto;

import lombok.Getter;

@Getter
public class PostModifyRequestDto {
    private Long userId;
    private String title;
    private String content;
}
