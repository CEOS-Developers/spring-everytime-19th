package com.ceos19.springboot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class PostParam {
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
