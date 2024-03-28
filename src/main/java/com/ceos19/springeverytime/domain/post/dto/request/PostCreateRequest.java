package com.ceos19.springeverytime.domain.post.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCreateRequest {
    private final String title;
    private final String content;
    private final boolean isAnonymous;

    public static PostCreateRequest of(String title, String content, boolean isAnonymous) {
        return new PostCreateRequest(
                title, content, isAnonymous
        );
    }
}
