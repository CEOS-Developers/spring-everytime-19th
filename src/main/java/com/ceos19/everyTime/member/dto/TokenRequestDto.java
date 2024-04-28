package com.ceos19.everyTime.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenRequestDto {
    @NotBlank(message = "토큰 재발급을 위하여 accessToken의 값을 입력해주세요.")
    private String accessToken;

    @NotBlank(message = "토큰 재발급을 위하여 refreshToken의 값을 입력해주세요.")
    private String refreshToken;
}