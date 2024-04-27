package com.ceos19.everytime.auth.service;

import java.util.Date;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.security.JwtUtil;
import com.ceos19.everytime.security.RefreshToken;
import com.ceos19.everytime.security.RefreshTokenRepository;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshRepository;

    @Transactional
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        // get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            // response status code
//            return new ResponseEntity<>("refresh token null", BAD_REQUEST);
            log.warn("refresh token null");
            throw new IllegalArgumentException("refresh token null");
        }

        // expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            //response status code
//            return new ResponseEntity<>("refresh token expired", BAD_REQUEST);
            log.warn("refresh token expired");
            throw new IllegalArgumentException("refresh token expired");
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!"refresh".equals(category)) {
            //response status code
//            return new ResponseEntity<>("invalid refresh token", BAD_REQUEST);
            log.warn("invalid refresh token");
            throw new IllegalArgumentException("invalid refresh token");
        }

        // DB에 저장되어 있는지 확인
        if (!refreshRepository.existsByRefreshToken(refresh)) {
            // response body
//            return new ResponseEntity<>("invalid refresh token", BAD_REQUEST);
            log.warn("invalid refresh token");
            throw new IllegalArgumentException("invalid refresh token");
        }


        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role);
        String newRefresh = jwtUtil.createJwt("refresh", username, role);

        // Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefreshToken(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);

        // response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        log.info("reissue success");
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshEntity = new RefreshToken(username, refresh, date.toString());

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        // cookie.setSecure(true);
        // cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
