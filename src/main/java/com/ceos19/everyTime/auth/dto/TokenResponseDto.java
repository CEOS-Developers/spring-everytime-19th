package com.ceos19.everyTime.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
@AllArgsConstructor
public class TokenResponseDto {
    private String grantType;
    private String accessToken;
    private ResponseCookie responseCookie;
    private Long accessTokenExpirationTime;
}