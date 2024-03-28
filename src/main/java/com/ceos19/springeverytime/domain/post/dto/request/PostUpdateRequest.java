package com.ceos19.springeverytime.domain.post.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PostCreateRequest DTO와 동일한데 중복으로 생성한 이유
 * - 나중에 Create와 Update DTO 사이에 차이점이 발생하게 되는 요구사항이 추가되더라도 유연하게 대처하기 위해
 *   동일한 코드이지만 별도 파일로 생성하였습니다.
 * */

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PostUpdateRequest {
    @NotNull(message = "제목을 입력해주세요.")
    private final String title;
    @NotNull(message = "내용을 입력해주세요.")
    private final String content;
    @NotNull(message = "게시글의 익명 여부 데이터가 필요합니다.")
    private final boolean isAnonymous;

    public static PostUpdateRequest of(String title, String content, boolean isAnonymous) {
        return new PostUpdateRequest(
                title, content, isAnonymous
        );
    }
}
