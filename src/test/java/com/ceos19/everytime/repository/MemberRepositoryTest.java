package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.University;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository ;
    @Autowired
    UniversityRepository universityRepository;

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

    @DisplayName("회원이 올바르게 생성된다")
    @Test
    public void 회원가입(){
        //given
        Optional<Member> test1;
        Optional<Member> test2;
        Optional<Member> test3;

        //when
        test1 = memberRepository.findByUserName("amy");
        test2 = memberRepository.findByUserName("sarah");
        test3 = memberRepository.findByUserName("siyoung");

        //then
        assertEquals(member1.getId(), test1.get().getId());
        assertEquals(member2.getId(), test2.get().getId());
        assertEquals(member3.getId(), test3.get().getId());
    }

    @DisplayName("회원 이름으로 회원이 조회된다.")
    @Test
    public void 회원조회(){
        //given

        //when
        Optional<Member> test1 = memberRepository.findByUserName("amy");
        boolean actual = memberRepository.findByUserName("John").isPresent();

        //then
        test1.ifPresent(member -> assertEquals(member1.getId(), member.getId()));
        assertThat(actual).isFalse();
    }

}
