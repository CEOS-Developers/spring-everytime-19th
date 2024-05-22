package com.ceos19.everytime.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class CreateBoardRequest {

    @NotBlank(message = "게시판 이름을 작성해주세요.")
    private String boardName;

    @NotBlank(message = "게시판 설명을 작성해주세요.")
    private String description;

    @NotNull(message = "게시판 관리자 이이디를 입력해주세요")
    private Long boardManagerId;

    @NotNull(message = "대학교 이이디를 입력해주세요")
    private Long universityId;

}
