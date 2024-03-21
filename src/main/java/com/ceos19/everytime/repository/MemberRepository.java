package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserName(String userName);
    Optional<Member> findByLoginId(String loginId);
}
