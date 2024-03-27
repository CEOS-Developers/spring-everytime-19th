package com.ceos19.springeverytime.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryUpdateRequest {
    private String description;

    @Builder
    private CategoryUpdateRequest(String description) {
        this.description = description;
    }

    public static CategoryUpdateRequest from(String description) {
        return CategoryUpdateRequest.builder().description(description).build();
    }
}
