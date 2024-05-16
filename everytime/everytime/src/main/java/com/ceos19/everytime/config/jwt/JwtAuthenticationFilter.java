package com.ceos19.everytime.config.jwt;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class  JwtAuthenticationFilter  extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        //요청 헤더에서 키가 'Authorization'인 필드의 값을 가져오기
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        //가져온 필드의 값에서 접두사 'Bearer'를 제외한 값을 token으로 얻기
        String token = getAccessToken(authorizationHeader);

        //얻은 token이 유효한지 검사
        if (tokenProvider.validateAccessToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            //유효하다면 인증 정보를 관리하는 security context holder에 인증 정보를 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        //만일 헤더에서 'Authorization'의 값이 null이 아니고 Bearer로 시작한다면
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            //token만 가져오기 위해 "Bearer "를 제외한 문자열을 반환
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }

        //null이거나 Bearer로 시작 안한다면 null을 반환
        return null;
    }
    }

