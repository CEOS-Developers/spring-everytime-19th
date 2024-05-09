package com.ceos19.everytime.jwt;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshRepository;

    public void validateRefreshToken(String refresh) {
        validateRefreshTokenNull(refresh);
        validateTokenExpired(refresh);
        validateRefreshTokenCategory(refresh);
        validateRefreshTokenValidity(refresh);
    }

    public void validateTokenExpired(final String token) {
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException(jwtUtil.getCategory(token) + " token expired");
        }
    }

    public void validateAccessToken(final String accessToken) {
        if (!jwtUtil.isEqualToAccessTokenCategory(accessToken)) {
            throw new IllegalArgumentException("Invalid access token");
        }
    }

    private void validateRefreshTokenValidity(String refresh) {
        if (!refreshRepository.existsByRefreshToken(refresh)) {
            throw new IllegalArgumentException("invalid refresh token");
        }
    }

    private void validateRefreshTokenCategory(String refresh) {
        if (!jwtUtil.isEqualToRefreshTokenCategory(refresh)) {
            throw new IllegalArgumentException("invalid refresh token");
        }
    }

    private void validateRefreshTokenNull(String refresh) {
        if (refresh == null) {
            throw new IllegalArgumentException("refresh token null");
        }
    }
}
