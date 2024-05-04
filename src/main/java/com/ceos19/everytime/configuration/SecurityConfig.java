package com.ceos19.everytime.configuration;

import com.ceos19.everytime.jwt.JwtFilter;
import com.ceos19.everytime.jwt.JwtUtil;
import com.ceos19.everytime.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    @Bean  //AuthenticationManager Bean 등록
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean  // passwordEncoder 빈 등록
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean  // 필터 등록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http.csrf(AbstractHttpConfigurer::disable);  // jwt는 session을 stateless하게 유지하므로 csrf에 대한 방어가 필요하지 않다

        //From 로그인 방식 disable -> UsernamePasswordAuthenticationFilter 커스텀 구현 필요
        http.formLogin(AbstractHttpConfigurer::disable);

        //http basic 인증 방식 disable
        http.httpBasic(AbstractHttpConfigurer::disable);

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**")
                .permitAll()// swagger 경로 접근 허용
                .requestMatchers("/login", "/", "/api/join").permitAll()  // root 경로 접근 허용 (추후 "**" 제거해야 함. 개발시 편의를 위해 설정)
                .requestMatchers("/admin").hasRole("ADMIN")  // ADMIN만 접근 허용
                .anyRequest().authenticated() // 이외의 경로는 로그인한 사용자만 접근 허용
        );

        // JwtFilter 등록
        // 로그인 전에 jwt 검증
        http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        // Form로그인 disable로 인해 기존에 설정 되었던 UsernamePasswordAuthenticationFilter가 사용되지 않으므로
        // 새로이 생성한 커스텀 필터(LoginFilter)를 해당 필터 자리에 대신 해서 넣어줌
        // ~/login에 대한 post 요청은 여기에서 처리
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션 stateless 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

