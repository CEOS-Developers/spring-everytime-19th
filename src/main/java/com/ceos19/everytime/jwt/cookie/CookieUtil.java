package com.ceos19.everytime.jwt.cookie;

import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CookieUtil {
    public String getRefreshToken(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {

                return cookie.getValue();
            }
        }
        log.error("에러 내용: Refresh Token에서 'refresh' 헤더 조회 실패 " +
                        "발생 원인: 쿠키에 'refresh'라는 이름의 정보가 등록되어 있지 않음");
        throw new AppException(ErrorCode.REFRESH_NOT_EXIST, "refresh token 정보가 존재하지 않습니다");
    }

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);

        cookie.setHttpOnly(true);  // HttpOnly 설정을 통해서 프론트 단에서 javascript로 cookie에 접근을 할 수 없도록 함.

        return cookie;
    }
}
