package com.ceos19.springboot.post.dto;

import lombok.Getter;

@Getter
public class CombinedDto {
    private PostUserRequestDto user;
    private PostRequestDto post;
}
