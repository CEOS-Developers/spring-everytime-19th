package com.ceos19.everytime.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddPostRequest {
    @NotBlank
    private Long userId;  // request body 에 사용자 정보 포함

    @NotNull
    private String title;
    private String content;

    private boolean isQuestion;
    private boolean isAnonymous;
}
