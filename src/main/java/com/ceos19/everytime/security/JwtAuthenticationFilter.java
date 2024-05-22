package com.ceos19.everytime.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 모든 Security filter는 Bean으로 등록되어 ContextLoaderListener에 의해 로드된다.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 헤더 내부에서 JWT 용으로 사용 할 Key
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 인증 타입
    public static final String BEARER_PREFIX = "Bearer ";
    // JWT의 생성, 해독, 변환 등을 담당
    private final TokenProvider tokenProvider;


    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext에 저장
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Request Header에서 토큰을 꺼낸다
        String jwt = resolveToken(request);

        // validateToken으로 토큰 유효성 검사 - 정상토큰이면 해당 토큰으로 Authentication을 가져와서 SecurityContext에 저장
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // Authentication 객체 받아오기
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            // SecurityContextHoler에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보를 꺼내는 메서드
    private String resolveToken(HttpServletRequest request) {
        // 헤더에서 토큰 부분을 분리
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        // 해당 키에 해당하는 헤더가 존재하고, 그 값이 Bearer로 시작한다면 (즉 JWT가 있다면)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            // PREFIX 부분을 날리고 JWT만 token에 할당한다.
            return bearerToken.split(" ")[1].trim();
        }

        return null;
    }

}