package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.AboutToken.RefreshToken;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.exception.NotFoundException;
import com.ceos19.everytime.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ERROR));
    }
}
