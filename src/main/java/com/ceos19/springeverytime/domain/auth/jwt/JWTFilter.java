package com.ceos19.springeverytime.domain.auth.jwt;

import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.user.dto.response.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];
        if (jwtUtil.isExpired(token)) {
            System.out.println("token is expired");
            filterChain.doFilter(request, response);
            return;
        }

        String loginId = jwtUtil.getLoginId(token);

        User user = new User(
                loginId,
                "",
                "",
                "",
                "",
                "",
                ""
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
