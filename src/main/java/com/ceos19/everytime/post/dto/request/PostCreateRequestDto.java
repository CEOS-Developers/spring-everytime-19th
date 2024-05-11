package com.ceos19.everytime.post.dto.request;

import lombok.Builder;

@Builder
public record PostCreateRequestDto(String title, String content, boolean isAnonymous, Long boardId) {
}
