package com.ceos19.everytime.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class BoardUpdateRequest {

    @NotBlank(message = "변경할 게시판 이름을 작성해주세요.")
    private String boardName;

    @NotBlank(message = "변경할 게시판 설명을 작성해주세요.")
    private String description;
}
