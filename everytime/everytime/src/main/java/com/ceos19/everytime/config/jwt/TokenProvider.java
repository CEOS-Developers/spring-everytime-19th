package com.ceos19.everytime.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.client.RestTemplate;



import java.net.URI;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITY_KEY = "auth";
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    //Bean 생성 후 주입 받기
    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }


    //secret을 BASE64로 디코딩한 다음 key변수에 할당
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    //Authentication 객체의 권한 정보를 이용하여 액세스 토큰을 생성
    public String createAccessToken(Long id, Authentication authentication) {

        //authorities 설정
        String authorities =
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        //토큰 만료 시간 설정
        long now = (new Date()).getTime(); //현재 시각 구하기
        Date validDate = new Date(now + this.tokenValidityInMilliseconds); //현재 시각 + 토큰 유효 기간

        return Jwts.builder()
                .setSubject(authentication.getName()) //토큰의 용도를 명시
                .claim(AUTHORITY_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, key) //sign시 사용할 알고리즘과 key값
                .setExpiration(validDate) //토큰 만료시간 설정
                .compact(); //토큰을 생성
    }

    // 토큰 기반으로 User Id 가져오는 메서도
    public Long getTokenUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key).parseClaimsJws(token).getBody();
        return claims.get("id", Long.class);
    }

    //토큰을 받아 인증 정보를 담은 객체 Authentication을 반환
    public Authentication getAuthentication(String token) {
        //토큰을 이용하여 claim 생성
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        //claim을 이용하여 authorities 생성
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITY_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //claim과 authorities를 이용하여 User 객체 생성
        User object = new User(claims.getSubject(), "", authorities);

        //마지막으로 Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(object, token, authorities);
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key) //비밀값으로 복호화
                    .build().parseClaimsJws(token);
            return true;
        }
        //복호화 과정 시도했다가 에러가 나면 아래와 같이 경우에 따라 유효하지 않은 토큰으로 처리
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {log.info("잘못된 JWT 서명입니다.");}
        catch (ExpiredJwtException e) { log.info("만료된 JWT 토큰입니다."); }
        catch (UnsupportedJwtException e) { log.info("지원되지 않는 JWT 토큰입니다."); }
        catch (IllegalArgumentException e) { log.info("잘못된 JWT 토큰입니다."); }
        return false;
    }

}
