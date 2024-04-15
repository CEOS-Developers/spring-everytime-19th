package com.ceos19.everytime.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private String secret;
    private JwtUtil jwtUtil;
    private String jwt;

    @BeforeEach
    void setUp() {
        secret = "myseckrekey123456bfdpdsfgklsdfjl";
        jwtUtil = new JwtUtil(secret, 1000L);
        jwt = jwtUtil.createJwt("access", "username", "role");
    }

    @Test
    void 토큰을_생성한다() {
        assertThat(jwt).isNotNull();
    }

    @Test
    void 토큰에서_username을_추출한다() {
        assertThat(jwtUtil.getUsername(jwt)).isEqualTo("username");
    }

    @Test
    void 토큰에서_Role을_추출한다() {
        assertThat(jwtUtil.getRole(jwt)).isEqualTo("role");
    }

    @Test
    void 토큰에서_카테고리를_추출한다() {
        assertThat(jwtUtil.getCategory(jwt)).isEqualTo("access");
    }
}
