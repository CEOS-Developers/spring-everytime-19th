package com.ceos19.springeverytime.domain.category.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
public class CategoryUpdateRequest {
    @NotNull(message = "게시판 설명을 입력해주세요.")
    private final String description;

    @Builder
    @JsonCreator
    // https://kong-dev.tistory.com/236?category=1072302
    private CategoryUpdateRequest(final String description) {
        this.description = description;
    }

    public static CategoryUpdateRequest from(String description) {
        return CategoryUpdateRequest.builder().description(description).build();
    }
}
