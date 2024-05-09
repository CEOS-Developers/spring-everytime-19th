package com.ceos19.everytime.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class DeleteRequest {

    @NotNull(message = "회원 아이디를 적어주세요.")
    private Long memberId;
}