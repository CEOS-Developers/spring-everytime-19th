package com.ceos19.everytime.auth.controller;

import com.ceos19.everytime.auth.dto.response.ReissueResponse;
import com.ceos19.everytime.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private static final String REFRESH = "refresh";

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        final ReissueResponse reissueResponse = authService.reissue(getRefreshToken(request));
        response.setHeader("access", reissueResponse.newAccess());
        response.addCookie(createCookie(reissueResponse.newRefresh()));
        return ResponseEntity.ok().build();
    }

    private String getRefreshToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
            .filter(cookie -> REFRESH.equals(cookie.getName()))
            .map(Cookie::getValue)
            .findAny()
            .orElse(null);
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie(REFRESH, value);
        cookie.setMaxAge(24 * 60 * 60);
        // cookie.setSecure(true);
        // cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
