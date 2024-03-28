package com.ceos19.springboot.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {
    private Long postId;
    private String title;
    private Boolean anonymous;
    private Integer view;
    private Integer likes;

    public PostResponseDto(Long postId, String title, Boolean anonymous, Integer view, Integer likes) {
        this.postId = postId;
        this.title = title;
        this.anonymous = anonymous;
        this.view = view;
        this.likes = likes;
    }
}
