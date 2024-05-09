package com.ceos19.everytime.config;

import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import com.ceos19.everytime.config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    /**  CORS 설정: 웹 페이지가 다른 도메인의 리소스에 접근하도록 허용하는 메커니즘
     * 보안상의 이유로 기본적으로 웹 브라우저에서는 Same-Origin Policy를 시행하므로,
     * 웹 페이지는 원래 같은 도메인의 리소스만 사용할 수 있다.
     * CORS 설정을 통해 특정 출처에서 실행되는 웹 사이트에 대해 예외를 설정하는 것
     */
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*")); //모든 종류의 HTTP 헤더를 허용하도록 설정
            config.setAllowedMethods(Collections.singletonList("*")); //모든 종류의 HTTP 메소드를 허용하도록 설정
            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000")); // 허용할 origin
            config.setAllowCredentials(true); //인증 정보와 관련된 요청을 허용
            return config;
        };
    }
    //특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(
                        corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        requests -> requests.requestMatchers("/login")
                                .permitAll());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}