package com.ceos19.springeverytime.domain.post.dto.response;

import com.ceos19.springeverytime.domain.comment.dto.response.CommentResponse;
import com.ceos19.springeverytime.domain.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PostDetailResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final List<CommentResponse> comments;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;

    public static PostDetailResponse from(final Post post) {
        final List<CommentResponse> commentResponses = post.getComments().stream()
                .map(CommentResponse::from)
                .toList();

        return new PostDetailResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                commentResponses,
                post.getCreateDate(),
                post.getModifyDate()
        );
    }
}
