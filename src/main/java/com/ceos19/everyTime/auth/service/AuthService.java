package com.ceos19.everyTime.auth.service;

import com.ceos19.everyTime.auth.jwt.TokenProvider;
import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.auth.dto.SignInRequestDto;
import com.ceos19.everyTime.auth.dto.TokenDto;
import com.ceos19.everyTime.auth.dto.TokenResponseDto;
import com.ceos19.everyTime.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.savedrequest.CookieRequestCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String,String> redisTemplate;





    @Transactional
    public TokenResponseDto login(final SignInRequestDto signInRequestDto){

        Member findMember=memberRepository.findMemberByLoginId(signInRequestDto.getUserID()).orElseThrow(()->new NotFoundException(
                ErrorCode.MESSAGE_NOT_FOUND));

        if(!passwordEncoder.matches(signInRequestDto.getPassword(),findMember.getPassword())){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }


        UsernamePasswordAuthenticationToken authenticationToken= signInRequestDto.getAuthenticationToken();
        Authentication authentication= authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto=tokenProvider.createToken(authentication);

        redisTemplate.opsForValue().set(authentication.getName(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);

        return new TokenResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),
                makeResponseCookie(tokenDto.getRefreshToken()),tokenDto.getAccessTokenValidationTime());

    }


    @Transactional
    public TokenResponseDto reIssue(final String accessToken, final String refreshToken){
        Authentication authentication= tokenProvider.getAuthentication(accessToken);

        if(!redisTemplate.opsForValue().get(authentication.getName()).equals(refreshToken)){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        TokenDto tokenDto=tokenProvider.createToken(authentication);
        redisTemplate.opsForValue().set(authentication.getName(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);


        return new TokenResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),
                makeResponseCookie(tokenDto.getRefreshToken()),tokenDto.getAccessTokenValidationTime());
    }

    @Transactional
    public void logout(final String accessToken){

        if (!tokenProvider.validateToken(accessToken)){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        if (redisTemplate.opsForValue().get(authentication.getName())!=null){
            redisTemplate.delete(authentication.getName());
        }


        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken,"logout",expiration,TimeUnit.MILLISECONDS);
    }

    private ResponseCookie makeResponseCookie(String refreshToken){
        return  ResponseCookie.from("refreshToken",refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60)
                .domain("localhost")
                .build();
    }

}
