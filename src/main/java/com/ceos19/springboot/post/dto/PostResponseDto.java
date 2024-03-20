package com.ceos19.springboot.post.dto;

import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private Boolean anonymous;
    private Integer view;
    private Integer likes;

}
