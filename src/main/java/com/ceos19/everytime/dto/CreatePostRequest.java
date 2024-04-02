package com.ceos19.everytime.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class CreatePostRequest {

    @NotBlank(message = "게시글 제목을 작성해주세요.")
    private String title;

    @NotBlank(message = "게시글 내용을 작성해주세요.")
    private String content;

    @NotNull(message = "작성자 아이디를 적어주세요.")
    private Long authorId;

    @NotNull(message = "게시판 아이디를 적어주세요.")
    private Long boardId;

    @NotNull(message = "익명 여부를 적어주세요.")
    private boolean isAnonymous;

}
