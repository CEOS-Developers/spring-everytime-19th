package com.ceos19.everytime.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.springframework.lang.Nullable;


@Data
public class AddCommentRequest {
    @NotBlank
    private String content;
    private Long commenterId;
}
