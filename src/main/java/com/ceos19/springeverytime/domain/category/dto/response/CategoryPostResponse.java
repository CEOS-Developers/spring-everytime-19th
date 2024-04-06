package com.ceos19.springeverytime.domain.category.dto.response;

import com.ceos19.springeverytime.domain.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryPostResponse {
    private final String title;
    private final String content;
    private final Long likeCount;
    private final Long commentCount;
    private final LocalDateTime createDate;
    private final String authorNickname;

    public static CategoryPostResponse from(Post post) {
        return new CategoryPostResponse(
                post.getTitle(),
                post.getContent(),
                (long) post.getPostLikes().size(),
                (long) post.getComments().size(),
                post.getCreateDate(),
                post.isAnonymous() ? "익명" : post.getAuthor().getNickname()
        );
    }
}
