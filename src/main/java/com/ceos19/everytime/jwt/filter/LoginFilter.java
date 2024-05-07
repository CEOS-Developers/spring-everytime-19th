package com.ceos19.everytime.jwt.filter;

import com.ceos19.everytime.jwt.util.JwtUtil;
import com.ceos19.everytime.jwt.cookie.CookieUtil;
import com.ceos19.everytime.jwt.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenService refreshTokenService;

    private final Long accessExpirationMs = 600000L;
    private final Long refreshExpirationMs = 86400000L;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // request로 부터 username, password 가져오기
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // DB에서 user 정보를 가져와서 authToken에 대한 검증 진행
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        // 유저 정보
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 생성
        String access = jwtUtil.createToken("access", username, role, accessExpirationMs);
        String refresh = jwtUtil.createToken("refresh", username, role, refreshExpirationMs);

        // DB에 RefreshToken 정보 저장
        refreshTokenService.addRefreshToken(username, refresh, refreshExpirationMs);

        // 응답 설정
        response.addHeader("access", access);
        response.addCookie(cookieUtil.createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        String username = obtainUsername(request);
        // 로그인 실패 알림 log
        log.info("authentication fail\n - username: {}\n - time: {}", username, LocalDateTime.now());

        // header에 인증 실패 정보 담아서 반환
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
