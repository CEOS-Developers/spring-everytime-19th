package com.ceos19.everytime.jwt.controller;

import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.jwt.service.RefreshTokenService;
import com.ceos19.everytime.jwt.util.JwtUtil;
import com.ceos19.everytime.jwt.cookie.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    private final Long accessExpirationMs = 600000L;
    private final Long refreshExpirationMs = 86400000L;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            //HttpServletRequest로 앞단에서 오는 요청을 받음, HttpServletResponse로 반환함.

            //get refresh token
            String refresh = cookieUtil.getRefreshToken(request.getCookies());

            //expired check  // service에서 작동하도록 리팩토링 필요
            if (jwtUtil.isExpired(refresh)) {
                throw new AppException(ErrorCode.TOKEN_EXPIRED, "refresh token expired");
            }

            // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)  // service에서 작동하도록 리팩토링 필요
            String category = jwtUtil.getCategory(refresh);
            if (!category.equals("refresh")) {
                throw new AppException(ErrorCode.TOKEN_EXPIRED, "invalid refresh token");
            }

            // DB에 저장되어 있는지 확인
            refreshTokenService.checkRefreshTokenIsSavedByRefresh(refresh);


            String username = jwtUtil.getUsername(refresh);
            String role = jwtUtil.getRole(refresh);

            //make new JWT
            String newAccess = jwtUtil.createToken("access", username, role, accessExpirationMs); // Refresh Token으로 새로이 생성된 Access Token
            String newRefresh = jwtUtil.createToken("refresh", username, role, refreshExpirationMs);

            // 새롭게 생성한 refresh token을 쿠키에 담음
            Cookie refreshCookie = cookieUtil.createCookie("refresh", newRefresh); // Refresh Rotate로 생성된 Refresh Token

            // Refresh Token 저장 DB에 기존의 Refresh Token 삭제 후 새 Refresh Token 저장
            refreshTokenService.deleteRefreshToken(refresh);
            refreshTokenService.addRefreshToken(username, newRefresh, refreshExpirationMs);

            //response
            response.setHeader("access", newAccess);
            response.addCookie(refreshCookie);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AppException e) {
            BaseResponse value =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(value, e.getErrorCode().getHttpStatus());
        }
    }


}
