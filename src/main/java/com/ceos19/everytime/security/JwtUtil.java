package com.ceos19.everytime.security;

import static io.jsonwebtoken.Jwts.SIG.HS256;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final  long expiredMs;

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
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public String getRole(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public String getCategory(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }

    public boolean isExpired(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}
