package com.ceos19.everyTime.member.service;

import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.dto.MemberSignUpDto;
import com.ceos19.everyTime.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    //멤버 저장 메서드
    @Transactional
    public void saveMember(MemberSignUpDto memberSignUpDto){
        Member member=Member.builder()
                .name(memberSignUpDto.getName())
                    .loginId(memberSignUpDto.getLoginId())
                        .nickName(memberSignUpDto.getNickName())
                            .password(memberSignUpDto.getPassword())
                                .build();


        memberRepository.save(member);
    }

}
