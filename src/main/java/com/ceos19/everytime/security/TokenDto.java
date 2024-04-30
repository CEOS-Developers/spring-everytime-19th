package com.ceos19.everytime.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String grantType;           // grantType은 JWT 대한 인증 타입. Bearer 사용
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;
}
