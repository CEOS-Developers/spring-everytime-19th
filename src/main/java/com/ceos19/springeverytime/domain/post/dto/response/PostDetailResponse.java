package com.ceos19.springeverytime.domain.post.dto.response;

import com.ceos19.springeverytime.domain.post.domain.Post;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PostDetailResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;

    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getCreateDate(),
                post.getModifyDate()
        );
    }
}
