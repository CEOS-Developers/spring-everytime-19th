package com.ceos19.everytime.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class AddPostLikeRequest {
    @NotNull
    private Long userId;
}
