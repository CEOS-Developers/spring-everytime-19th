package com.ceos19.everytime.jwt.cookie;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public String getRefreshToken(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {

                return cookie.getValue();
            }
        }
        return null;
    }

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);

        cookie.setHttpOnly(true);  // HttpOnly 설정을 통해서 프론트 단에서 javascript로 cookie에 접근을 할 수 없도록 함.

        return cookie;
    }
}
