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


        //csrf enable하려면 csrf 토큰 설정같은 추가 작업이 필요
        // 특히 api 서버는 세션이 stateless 상태로 관리하므로 enable이 필요없음
        http.
                csrf((auth) -> auth.disable());

        // json 형태의 데이터를 받는 api에 적용 할 수 없기 때문
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
