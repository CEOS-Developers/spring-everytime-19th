package com.ceos19.springboot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddPostRequest {
    @NotNull
    private Long userId;

    private String title;
    private String content;

    private Long likeCount;
    private Long reportCount;
    private Long scrap;

    private Boolean isAnonymous;
    private String filePath;
}
