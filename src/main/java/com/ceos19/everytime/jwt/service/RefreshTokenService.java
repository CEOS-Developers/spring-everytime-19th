package com.ceos19.everytime.jwt.service;

import com.ceos19.everytime.domain.RefreshToken;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.jwt.repository.RefreshTokenRepository;
import com.ceos19.everytime.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.ceos19.everytime.exception.ErrorCode.NOT_NULL;
import static com.ceos19.everytime.exception.ErrorCode.NO_DATA_EXISTED;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public Long addRefreshToken(String username, String refresh, Long expiredMs) {
        // 예외 처리
        if (username == null || refresh == null || expiredMs == null) {
            log.error("에러 내용: Refresh Token DB 등록 실패 " +
                    "발생 원인: 매개변수로 null 값 사용");
            throw new AppException(NOT_NULL, "Refresh Token 등록시 매개변수에 null 값이 존재해서는 안됩니다");
        }
        userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("에러 내용: Refresh Token DB 등록 실패 " +
                    "발생 원인: 존재하지 않는 User의 username으로 등록 시도");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });

        Date expiredDate = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = RefreshToken.builder()
                .username(username)
                .refresh(refresh)
                .expiration(expiredDate.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getId();
    }

    public void checkRefreshTokenIsSavedByRefresh(String refresh) {
        if (!refreshTokenRepository.existsByRefresh(refresh)) {
            log.error("에러 내용: Refresh Token DB 등록 실패 " +
                    "발생 원인: 존재하지 않는 refresh token의 refresh로 등록 시도");
            throw new AppException(ErrorCode.REFRESH_NOT_EXIST, "해당 refresh 정보로 등록된 refresh token이 존재하지 않습니다");
        }
    }

    public void deleteRefreshToken(String refresh) {
        refreshTokenRepository.findByRefresh(refresh).orElseThrow(() -> {
            log.error("에러 내용: Refresh Token 제거 실패 " +
                    "발생 원인: 존재하지 않는 refresh token의 refresh로 조회");
            throw new AppException(ErrorCode.REFRESH_NOT_EXIST, "해당 refresh 정보로 등록된 refresh token이 존재하지 않습니다");
        });

        refreshTokenRepository.deleteByRefresh(refresh);
    }
}
