package com.ceos19.springeverytime.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder () {
        /**
         * 비밀번호 암호화를 위한 빈
         * */
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
        );

        http.formLogin((auth) -> auth
                .loginProcessingUrl("/login")
                .permitAll()
        );

        http.csrf((auth) -> auth.disable());

        return http.build();
    }
}
