package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Member;
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
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository ;


    @DisplayName("회원이 올바르게 생성된다")
    @Test
    public void saveUser(){
        //given
        Optional<Member> test1;
        Optional<Member> test2;

        Member member1 = new Member("amy", "abcd123", "password", "abcd123@gmail.com");
        Member member2 = new Member("sarah", "qwer123", "password", "qwer123@gmail.com");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        test1 = memberRepository.findByUserName("amy");
        test2 = memberRepository.findByUserName("sarah");

        //then
        assertEquals(member1.getId(), test1.get().getId());
        assertEquals(member2.getId(), test2.get().getId());
    }




}
