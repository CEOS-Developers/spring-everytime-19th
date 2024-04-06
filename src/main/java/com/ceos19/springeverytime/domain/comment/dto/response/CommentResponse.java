package com.ceos19.springeverytime.domain.comment.dto.response;

import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {
    private final Long id;
    private final User author; // user dto 로 변경해야 함.
    private final String content;
    private final LocalDateTime createDate;

    public static CommentResponse from(
            final Comment comment
    ) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getAuthor(),
                comment.getContent(),
                comment.getCreateDate()
        );
    }
}
