package com.ceos19.everytime.jwt.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    // JWT = Header.Payload.Verify Signature
    // 1) Header에서는 해시 알고리즘과 토큰의 타입을 정의함
    // 2) Payload는 전달하는 데이터를 포함함. 데이터는 키와 값으로 구성되어 있는데 여기서 키를 claim이라고 한다.
    //    payload는 누구나 디코딩하여 읽어볼 수 있기 때문에 password와 같은 민감한 정보는 포함해선 안된다.
    // 3) Verify Signature는 Base64로 인코딩된 header와 payload, 그리고 secret key를 해싱한 값이다.

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.token.secret}") String secretKey) {
        this.secretKey
                = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    /**
     * token에서 "username" claim을 꺼냄
     *
     * @param token
     * @return
     */
    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build() // secretKey를 통해 JWT parser를 생성
                .parseSignedClaims(token)  // parser로 token을 parsing함
                .getPayload().get("username", String.class); // parsing한 token에서 payload를 꺼낸뒤 "username" claim의 값을 가져옴.
    }

    /**
     * token에서 "password" claim을 꺼냄
     *
     * @param token
     * @return
     */
    public String getPassword(String token) {

        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("password", String.class); // parsing한 token에서 payload를 꺼낸뒤 "password" claim의 값을 가져옴. (+ 사실 만들긴 했는데 payload에 password가 안 들어가서 쓰일지는 모르겠다)
    }

    /**
     * token에서 "role" claim을 꺼냄
     *
     * @param token
     * @return
     */
    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("role", String.class); // parsing한 token에서 payload를 꺼낸뒤 "role" claim의 값을 가져옴.
    }

    /**
     * token에서 "category" 꺼냄
     *
     * @param token
     * @return
     */
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("category", String.class); // parsing한 token에서 payload를 꺼낸뒤 "category" claim의 값을 가져옴.
    }

    /**
     * 토큰 만료 여부 확인
     *
     * @param token
     * @return
     */
    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date()); // parsing한 token에서 payload에서 유효시간을 꺼내어 지금보다 이전값인지 (이미 만료되었는지)를 확인. 만료시 true, 아님 false
    }

    /**
     * 토큰 생성
     *
     * @param username
     * @param role
     * @param expiredMs
     * @return
     */
    public String createToken(String category, String username, String role, Long expiredMs) {
        Date currentTime = new Date(System.currentTimeMillis());
        Date expiredTime = new Date(currentTime.getTime() + expiredMs);

        return Jwts.builder()
                .claim("category", category)
                .claim("username", username) // payload에 "username":username 담음
                .claim("role", role) // payload에 "role":role 담음
                .issuedAt(currentTime)  // 발행 시간
                .expiration(expiredTime)  // 만료시간
                .signWith(secretKey)  // 키로 암호화
                .compact();  // 토큰 생성
    }

}
