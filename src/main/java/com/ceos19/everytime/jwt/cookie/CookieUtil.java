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
}
