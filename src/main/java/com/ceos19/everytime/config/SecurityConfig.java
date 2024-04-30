package com.ceos19.everytime.config;

import com.ceos19.everytime.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    // 메서드 체이닝의 사용을 지양하고 람다식을 통해 함수형으로 설정하게 지향하고 있음 (deprecated 대비)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                /*.headers((headerConfig) ->
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )*/
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                // 위 필터들은 순차적으로 실행되므로 순서에 유의
                                //.requestMatchers(PathRequest.toH2Console()).permitAll()
                                .requestMatchers("/", "/login/**", "/signup/**").permitAll()  // 홈페이지, 로그인, 회원가입 페이지에 대해서는 인증없이 접근이 가능해야 한다.
                                .requestMatchers("/admins/**", "/api/v1/admins/**").hasRole("ADMIN")        // 자동으로 'ROLE_'이 붙음
                                .requestMatchers("/api/v1/**").hasAnyRole("USER", "ADMIN")
                                .anyRequest().authenticated()
                )
               /* .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint).accessDeniedHandler(accessDeniedHandler)
                )*/
                .formLogin((formLogin) ->           // 폼로그인을 사용하기 위함
                        formLogin
                                .loginPage("/login/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginProcessingUrl("/login")   // login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행
                                .defaultSuccessUrl("/", true)
                )
                ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}