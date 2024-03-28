package com.ceos19.springeverytime.domain.category.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryCreateRequest {
    @NotNull(message = "게시판 이름을 입력해주세요.")
    private String name;
    private String description;

    @Builder
    private CategoryCreateRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static CategoryCreateRequest of(String name, String description) {
        return CategoryCreateRequest.builder().name(name).description(description).build();
    }
}
