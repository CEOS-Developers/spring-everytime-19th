package com.ceos19.everytime.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
public class ModifyPostRequest {
    @NotNull(message = "content must be not null")
    private String content;
    @NotNull(message = "isQuestion must have a value")
    private Boolean isQuestion;
    @NotNull(message = "isAnonymous must have a value")
    private Boolean isAnonymous;
}
