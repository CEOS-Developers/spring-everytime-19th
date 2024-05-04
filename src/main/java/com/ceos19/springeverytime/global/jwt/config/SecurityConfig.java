package com.ceos19.springeverytime.global.jwt.config;

import com.ceos19.springeverytime.global.jwt.filter.CustomLogoutFilter;
import com.ceos19.springeverytime.global.jwt.filter.JwtFilter;
import com.ceos19.springeverytime.global.jwt.filter.LoginFilter;
import com.ceos19.springeverytime.global.jwt.repository.RefreshRepository;
import com.ceos19.springeverytime.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.
                csrf((auth) -> auth.disable());

        http.
                formLogin((auth) -> auth.disable());

        http.
                httpBasic((auth) -> auth.disable());
        http.
                authorizeHttpRequests((auth) -> auth
                        .requestMatchers("api/users/login","api/users/join").permitAll()
                        .requestMatchers("/api/category").hasRole("ADMIN")
                        .requestMatchers("/reissue").permitAll()
                        .anyRequest().authenticated()
                );
        http
                .addFilterBefore(new JwtFilter(jwtUtil), LogoutFilter.class);
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);

        http.
                sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }
}
