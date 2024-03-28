package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.University;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.UniversityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberRepository memberRepository ;
    @Autowired UniversityRepository universityRepository;
    @Autowired MemberService memberService;

    Member member1;
    Member member2;
    Member member3;
    University university;

    @BeforeEach
    void setup(){
        university = new University("홍익대학교");
        universityRepository.save(university);
        member1 = new Member("amy", "abcd123", "password", "abcd123@gmail.com", university);
        member2 = new Member("sarah", "qwer123", "password", "qwer123@gmail.com", university);
        member3 = new Member("siyoung", "abcsdw", "password", "abcsdw@gmail.com",university);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
    }

    @DisplayName("회원가입이 올바르게 진행된다")
    @Test
    public void 회원가입(){
        //given

        //when
        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);

        //then
        assertEquals(3, memberRepository.count());
    }

    @DisplayName("중복 이름 불가")
    @Test
    public void 중복_회원_불가(){
        //given
        Member test1 = new Member("siyoung", "abcsdw", "password", "abcsdw@gmail.com",university);

        //when
        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);
        memberService.join(test1);

        //then
        assertEquals(3, memberRepository.count());
    }

    @DisplayName("이름 변경")
    @Test
    public void 멤버_이름_변경(){
        //given
        memberService.join(member1);
        String newname = "james";

        //when
        memberService.updateUsername(member1.getId(), newname);

        //then
        assertEquals(newname, member1.getUsername());

    }

    @DisplayName("멤버 삭제")
    @Test
    public void 회원_탈퇴(){
        //given
        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);

        //when
        memberService.deleteUser(member1.getId());

        //then
        assertEquals(2, memberRepository.count());
    }
}
