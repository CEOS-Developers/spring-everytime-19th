package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByUsernameAndLoginId(String username, String loginId);
    Boolean existsByLoginIdAndUserPw(String loginId, String userPw);

}
