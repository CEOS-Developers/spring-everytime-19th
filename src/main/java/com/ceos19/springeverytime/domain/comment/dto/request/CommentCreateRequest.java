package com.ceos19.springeverytime.domain.comment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentCreateRequest {
    @NotNull(message = "댓글 내용을 입력해주세요.")
    private final String content;

    @NotNull(message = "익명 여부를 설정해주세요.")
    private final boolean isAnonymous;
}
