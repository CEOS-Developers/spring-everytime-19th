package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(Member member) {
        Optional<Member> byUserName = memberRepository.findByUsername(member.getUsername());
        Optional<Member> byUserId = memberRepository.findByLoginId(member.getLoginId());

        if (byUserName.isEmpty() && byUserId.isEmpty()) {
            log.info("[Service][join] SUCCESS    username:{}", byUserName.get().getUsername());
            return member;
        }
        else if(byUserName.isPresent()){
            log.info("[Service][join] DUPLICATE USERNAME    username:{}", byUserName.get().getUsername());
        }
        else{
            log.info("[Service][join] DUPLICATE USERID    LoginId:{}", byUserName.get().getLoginId());
        }

        return null;      // 중복으로 인해 회원가입 불가
    }

    public Member login(String loginId, String userPw){
        Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);

        if(byLoginId.isPresent()){
            if(userPw.equals(byLoginId.get().getUserPw())){
                log.info("[Service][login] SUCCESS    username:{}", byLoginId.get().getUsername());
                return byLoginId.get();
            }
        }

        log.info("[Service][login] LOGIN Fail");
        return null;
    }

    public Member findByLoginId(String loginId) {
        Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);

        if (byLoginId.isEmpty()) {
            log.info("[Service][findByLoginId] SUCCESS    username:{}", byLoginId.get().getUsername());
            return byLoginId.get();
        }

        log.info("[Service][findByLoginId] FAIL    username:{}", byLoginId.get().getUsername());
        return null;
    }

    public void updateUsername(final Long memberId, final String userName) {
        Optional<Member> byMemberId = memberRepository.findById(memberId);
        if(byMemberId.isPresent()){
            log.info("[Service][updateUsername] SUCCESS    username:{}   updated:{}", byMemberId.get().getUsername(), userName);
            byMemberId.get().changeUsername(userName);
        }
        else{
            log.info("[Service][updateUsername] FAIL");
        }
    }

    public void deleteUser(final Long memberId) {
        Optional<Member> byMemberId = memberRepository.findById(memberId);

        if(byMemberId.isPresent()){
            log.info("[Service][deleteUser] SUCCESS    username:{}", byMemberId.get().getUsername());
            memberRepository.deleteById(memberId);
        }

        log.info("[Service][deleteUser] FAIL");
    }

}
