package com.ceos19.everytime.dto.request;

import lombok.Builder;

@Builder
public record PostCreateRequestDto(String title, String content, boolean isAnonymous, Long boardId, Long userId) {
}
