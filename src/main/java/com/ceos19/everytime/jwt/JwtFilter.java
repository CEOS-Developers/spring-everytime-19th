package com.ceos19.everytime.jwt;

import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {  // JWT 검증 필터
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에서 Authorization Header를 찾음.
        String authorization = request.getHeader("Authorization");

        // 인증이 실패하는 경우
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("authorization header not present");

            // 연결된 다음 필터에 request, response를 넘겨줌
            filterChain.doFilter(request, response);

            // 메서드 종료
            return;
        }

        // 인증 시작
        System.out.println("authorization now");
        String token = authorization.substring("Bearer ".length());  // "Bearer " 제거

        // token이 만료된 경우
        if (jwtUtil.isExpired(token)) {
            System.out.println("token is expired");
            filterChain.doFilter(request, response);

            return;
        }

        // token에서 username과 role을 가져옴
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // 세션에 사용할 유저 객체 생성
        User user = User.createTempUser(username, "tempPassword", role);

        // UserDetails에 유저 정보 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
