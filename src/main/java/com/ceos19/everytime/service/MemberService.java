package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(Member member) {
        if((memberRepository.findByUsernameAndLoginId(member.getUsername(), member.getLoginId()).isPresent())){
            throw new CustomException(DATA_ALREADY_EXISTED);
        }

        return memberRepository.save(member)
                .getId();
    }

    public void login(String loginId, String userPw){

        if(!memberRepository.existsByLoginIdAndUserPw(loginId,userPw)){
            throw new CustomException(MEMBER_NOT_FOUND);
        }

    }

    @Transactional(readOnly = true)
    public Long findByLoginId(String loginId) {
        final Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return member.getId();
    }

    public void updateUsername(final Long memberId, final String userName) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        member.changeUsername(userName);
    }

    public void deleteUser(final Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        memberRepository.deleteById(memberId);
    }

}
