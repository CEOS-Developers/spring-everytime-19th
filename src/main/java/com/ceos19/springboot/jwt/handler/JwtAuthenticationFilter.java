package com.ceos19.springboot.jwt.handler;

import com.ceos19.springboot.jwt.TokenProvider;
import com.ceos19.springboot.users.UserDetailsImpl;
import com.ceos19.springboot.users.domain.UserRoleEnum;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.users.dto.UserRequestDto;
import com.ceos19.springboot.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        setFilterProcessesUrl("/api/user/login");  // 로그인 URL 설정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            UserRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserRequestDto.class);     // 2.
            System.out.println("requestDto.getLoginId() = " + requestDto.getLoginId());
            return getAuthenticationManager().authenticate(         // 3.
                    new UsernamePasswordAuthenticationToken(requestDto.getLoginId(), requestDto.getPassword(),null)    // 4.
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // 로그인 성공 시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();          	// username
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();      // role

        String token = tokenProvider.createAccessToken(username, authResult);     // 5.
        response.addHeader("Authorization", token);        // 6.
    }

    // 로그인 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("로그인 실패");
        log.error(failed.getMessage());
        response.setStatus(401);
    }
}
