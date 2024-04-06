package com.ceos19.springeverytime.domain.post.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCreateRequest {
    @NotNull(message = "제목을 입력해주세요.")
    private final String title;
    @NotNull(message = "내용을 입력해주세요.")
    private final String content;
    @NotNull(message = "게시글의 익명 여부 데이터가 필요합니다.")
    private final boolean isAnonymous;
    @NotNull(message = "게시판 아이디는 필수 입니다.")
    private final Long categoryId;
    private final List<Long> imageIds;

    public static PostCreateRequest of(String title, String content, boolean isAnonymous, Long categoryId, List<Long> imageIds) {
        return new PostCreateRequest(
                title, content, isAnonymous, categoryId, imageIds
        );
    }
}
