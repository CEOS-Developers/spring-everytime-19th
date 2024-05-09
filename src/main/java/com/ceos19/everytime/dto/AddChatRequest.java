package com.ceos19.everytime.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AddChatRequest {
    @NotNull
    private String content;
    @NotNull
    private Long userId;
}
