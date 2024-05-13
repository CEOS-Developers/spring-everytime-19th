package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Authority;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.dto.member.InfoUpdateRequest;
import com.ceos19.everytime.dto.member.MemberDto;
import com.ceos19.everytime.dto.member.SignUpRequest;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.security.CustomUserDetails;
import com.ceos19.everytime.security.TokenDto;
import com.ceos19.everytime.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenDto login (String loginId, String password) {

        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        //log.info("request loginId = {}, password = {}", loginId, password);
        log.info("jwtToken accessToken = {}", tokenDto.getAccessToken());

        return tokenDto;
    }

    @Transactional
    public MemberDto signUp(SignUpRequest signUpRequest) {

        if (memberRepository.existsByUsernameAndPassword(signUpRequest.getUsername(), signUpRequest.getPassword())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }

        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        //log.info("password original = {}, encoded = {}", signUpRequest.getPassword(), encodedPassword);

        return MemberDto.toDto(memberRepository.save(signUpRequest.toEntity(encodedPassword, Authority.ROLE_USER)));
    }

    @Transactional(readOnly = true)
    public Long findByLoginId(String username) {
        final Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return member.getId();
    }

    @Transactional
    public void updateNickname(final CustomUserDetails customUserDetails, final InfoUpdateRequest infoUpdateRequest) {

        final Member member = customUserDetails.getMember();
        member.changeNickname(infoUpdateRequest.getNickname());
    }

    @Transactional
    public void deleteUser(final Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        memberRepository.deleteById(memberId);
    }

}
