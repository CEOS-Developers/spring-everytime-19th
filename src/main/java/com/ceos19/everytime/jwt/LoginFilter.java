package com.ceos19.everytime.jwt;

import com.ceos19.everytime.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

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
        // 타입 캐스팅을 통해서 UserDetails를 상속받은 CustomUserDetails로 타입 변경
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        // 로그인 성공 알림 log
        log.info("authentication success\n - username: {}\n - time: {}", username, LocalDateTime.now());


        // 사용자의 Role 정보
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        log.warn(role);

        // access token 만료 시간
        long expiredMs = 60 * 60 * 1000L;
        String token = jwtUtil.createToken(username, role, expiredMs);

        // header에 토큰 담아서 반환. RFC 7235에서 정의 돼 있듯이, 접두사 Bearer를 붙여서 Authorization 헤더를 반환한다.
        response.addHeader("Authorization", "Bearer " + token);
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
