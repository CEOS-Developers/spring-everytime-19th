package com.ceos19.everytime.jwt;

import static io.jsonwebtoken.Jwts.SIG.HS256;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtil {

    private static final String REFRESH = "refresh";
    private static final String ACCESS = "access";

    private final SecretKey secretKey;
    private final long expiredMs;

    public JwtUtil(@Value("${spring.jwt.secret}") final String secret,
                   @Value("${spring.jwt.expired}") final long expiredMs) {
        final String algorithm = HS256.key()
                .build()
                .getAlgorithm();
        this.secretKey = new SecretKeySpec(secret.getBytes(UTF_8), algorithm);
        this.expiredMs = expiredMs;
    }

    public String createJwt(final String category, final String username, final String role) {
        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String getUsername(final String token) {
        return getPayload(token).get("username", String.class);
    }

    public String getRole(final String token) {
        return getPayload(token).get("role", String.class);
    }

    public String getCategory(final String token) {
        return getPayload(token)
                .get("category", String.class);
    }

    public boolean isExpired(final String token) {
        return getPayload(token)
                .getExpiration()
                .before(new Date());
    }

    public void validateAccessToken(final String accessToken) {
        if (!ACCESS.equals(getCategory(accessToken))) {
            throw new IllegalArgumentException("Invalid access token");
        }
    }

    public boolean isEqualToRefreshTokenCategory(final String refreshToken) {
        return REFRESH.equals(getCategory(refreshToken));
    }

    public void validateTokenExpired(final String token) {
        try {
            isExpired(token);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException(getCategory(token) + " token expired");
        }
    }

    private Claims getPayload(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
