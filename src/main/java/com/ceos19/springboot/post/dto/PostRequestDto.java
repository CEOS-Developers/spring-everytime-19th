package com.ceos19.springboot.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PostRequestDto {
    private String title;
    private String content;
    private Boolean anonymous;
}
