package com.ceos19.everytime.service;


import com.ceos19.everytime.config.jwt.TokenProvider;
import com.ceos19.everytime.domain.AboutUser.User;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validateAccessToken(refreshToken)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ERROR);
        }

        // DB에 저장되어 있던 refresh token과 일치하는지 확인
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        return tokenProvider.createAccessToken(authentication);
    }



}
