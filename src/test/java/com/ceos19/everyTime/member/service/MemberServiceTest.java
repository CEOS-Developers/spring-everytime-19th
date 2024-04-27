package com.ceos19.everyTime.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.dto.MemberSignUpDto;
import com.ceos19.everyTime.member.repository.MemberRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("member 저장 테스트")
    void saveMember() {
        //given
        MemberSignUpDto memberSignUpDto = MemberSignUpDto.builder()
                        .name("이진우")
                                .nickName("dionisos198")
                                        .loginId("dionisos198")
                                                .password("pppp")
                                                        .build();

        Member member = Member.builder()
                        .name(memberSignUpDto.getName())
                                .loginId(memberSignUpDto.getLoginId())
                                        .password(memberSignUpDto.getPassword())
                                                .nickName(memberSignUpDto.getNickName())
                                                        .build();

        ReflectionTestUtils.setField(member,"id",1L);

        when(memberRepository.save(any(Member.class))).thenReturn(member);
        //when
        Long findMemberId = memberService.saveMember(memberSignUpDto);

        //then
        Assertions.assertThat(findMemberId).isEqualTo(1L);



    }
}