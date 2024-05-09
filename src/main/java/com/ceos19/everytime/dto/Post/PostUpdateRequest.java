package com.ceos19.everytime.dto.Post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class PostUpdateRequest {

    @NotBlank(message = "변경할 게시글 제목을 작성해주세요.")
    private String title;

    @NotBlank(message = "변경할 게시글 내용을 작성해주세요.")
    private String content;

    @NotNull(message = "익명 여부를 적어주세요.")
    private boolean isAnonymous;
}
