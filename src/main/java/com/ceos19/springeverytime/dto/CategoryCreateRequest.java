package com.ceos19.springeverytime.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryCreateRequest {
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
