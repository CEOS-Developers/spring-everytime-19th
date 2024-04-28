package com.ceos19.everyTime.member.service;

import com.ceos19.everyTime.common.jwt.TokenProvider;
import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.dto.SignInDto;
import com.ceos19.everyTime.member.dto.TokenDto;
import com.ceos19.everyTime.member.dto.TokenRequestDto;
import com.ceos19.everyTime.member.dto.TokenResponseDto;
import com.ceos19.everyTime.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public TokenResponseDto login(SignInDto signInDto){

        Member findMember=memberRepository.findMemberByLoginId(signInDto.getUserID()).orElseThrow(()->new NotFoundException(
                ErrorCode.MESSAGE_NOT_FOUND));

        if(!passwordEncoder.matches(signInDto.getPassword(),findMember.getPassword())){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        UsernamePasswordAuthenticationToken authenticationToken=signInDto.getAuthenticationToken();
        Authentication authentication= authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto=tokenProvider.createToken(authentication);

        redisTemplate.opsForValue().set(authentication.getName(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);
        return new TokenResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),tokenDto.getRefreshToken(),tokenDto.getAccessTokenValidationTime());

    }


    @Transactional
    public TokenResponseDto reIssue(TokenRequestDto tokenRequestDto){
        String accessToken=tokenRequestDto.getAccessToken();
        String refreshToken=tokenRequestDto.getRefreshToken();
        Authentication authentication= tokenProvider.getAuthentication(accessToken);

        if(!redisTemplate.opsForValue().get(authentication.getName()).equals(refreshToken)){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        TokenDto tokenDto=tokenProvider.createToken(authentication);
        redisTemplate.opsForValue().set(authentication.getName(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);

        return new TokenResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),tokenDto.getRefreshToken(),tokenDto.getAccessTokenValidationTime());
    }

    @Transactional
    public void logout(TokenRequestDto tokenRequestDto){

        if (!tokenProvider.validateToken(tokenRequestDto.getAccessToken())){
            throw new IllegalArgumentException("로그아웃 : 유효하지 않은 토큰입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        if (redisTemplate.opsForValue().get(authentication.getName())!=null){
            redisTemplate.delete(authentication.getName());
        }




        Long expiration = tokenProvider.getExpiration(tokenRequestDto.getAccessToken());
        redisTemplate.opsForValue().set(tokenRequestDto.getAccessToken(),"logout",expiration,TimeUnit.MILLISECONDS);
    }

}
