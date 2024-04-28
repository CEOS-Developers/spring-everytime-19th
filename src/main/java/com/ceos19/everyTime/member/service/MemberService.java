package com.ceos19.everyTime.member.service;

import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.dto.MemberSignUpDto;
import com.ceos19.everyTime.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //멤버 저장 메서드
    @Transactional
    public Long saveMember(MemberSignUpDto memberSignUpDto){

        validateDuplicateMember(memberSignUpDto.getLoginId());

        Member member = Member.createNormalMember(memberSignUpDto.getName(),
                memberSignUpDto.getLoginId(), passwordEncoder.encode(memberSignUpDto.getPassword()),memberSignUpDto.getNickName());

        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    @Transactional
    public Long saveManager(MemberSignUpDto memberSignUpDto){

        validateDuplicateMember(memberSignUpDto.getLoginId());


        Member member = Member.createManager(memberSignUpDto.getName(), memberSignUpDto.getLoginId(),passwordEncoder.encode(memberSignUpDto.getPassword()),
                memberSignUpDto.getNickName());


        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    private void validateDuplicateMember(String loginId){
        if(memberRepository.existsByLoginId(loginId)){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }
    }

}
