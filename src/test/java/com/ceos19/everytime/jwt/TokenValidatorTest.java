package com.ceos19.everytime.jwt;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenValidatorTest {

    private String secret;
    private JwtUtil jwtUtil;
    private String jwt;
    private TokenValidator tokenValidator;

    @BeforeEach
    void setUp() {
        secret = "myseckrekey123456bfdpdsfgklsdfjl";
        jwtUtil = new JwtUtil(secret, 1000L);
        jwt = jwtUtil.createJwt("access", "username", "role");
        tokenValidator = new TokenValidator(jwtUtil, null);
    }

    @Test
    void 토큰의_카테고리가_access가_아니면_예외가_발생한다() {
        final String jwt = jwtUtil.createJwt("refresh", "username", "role");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tokenValidator.validateAccessToken(jwt))
                .withMessage("Invalid access token");
    }
}
