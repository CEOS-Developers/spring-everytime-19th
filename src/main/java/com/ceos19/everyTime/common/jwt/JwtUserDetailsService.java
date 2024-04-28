package com.ceos19.everyTime.common.jwt;

import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findMemberByLoginId(username)
                .map(this::getUserDetails)
                .orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    public UserDetails getUserDetails(Member member) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(member.getLoginId(), member.getPassword(), Collections.singleton(authority));
    }
}