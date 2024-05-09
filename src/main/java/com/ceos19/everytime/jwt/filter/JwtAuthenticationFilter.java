package com.ceos19.everytime.jwt.filter;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import com.ceos19.everytime.jwt.JwtUtil;
import com.ceos19.everytime.jwt.LoginDto;
import com.ceos19.everytime.jwt.RefreshToken;
import com.ceos19.everytime.jwt.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            LoginDto loginDto = objectMapper.readValue(messageBody, LoginDto.class);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());

            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("Request body parsing error");
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                            final FilterChain chain, final Authentication authResult)
            throws IOException, ServletException {
        log.info("successfulAuthentication");
        final String username = authResult.getName();

        String role = authResult.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        // 토큰 생성
        String access = jwtUtil.createJwt("access", username, role);
        String refresh = jwtUtil.createJwt("refresh", username, role);

        // refresh 토큰 저장
        addRefreshToken(username, refresh, 86400000L);

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(OK.value());
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                              final AuthenticationException failed) throws IOException, ServletException {
        log.info("unsuccessfulAuthentication");
        super.unsuccessfulAuthentication(request, response, failed);
        response.setStatus(SC_UNAUTHORIZED);
    }

    private void addRefreshToken(final String username, final String refresh, final Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        final RefreshToken refreshToken = new RefreshToken(refresh, username, date.toString());
        refreshTokenRepository.save(refreshToken);
    }

    private Cookie createCookie(final String key, final String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true); https를 사용할 때 설정
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
