package com.ceos19.everytime.jwt.controller;

import com.ceos19.everytime.jwt.util.JwtUtil;
import com.ceos19.everytime.jwt.cookie.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
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

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //HttpServletRequest로 앞단에서 오는 요청을 받음, HttpServletResponse로 반환함.

        //get refresh token
        String refresh = cookieUtil.getRefreshToken(request.getCookies());

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createToken("access", username, role, accessExpirationMs); // Refresh Token으로 새로이 생성된 Access Token
        Cookie newRefresh = cookieUtil.createCookie("refresh",
                jwtUtil.createToken("refresh", username, role, refreshExpirationMs)); // Refresh Rotate로 생성된 Refresh Token

        //response
        response.setHeader("access", newAccess);
        response.addCookie(newRefresh);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
