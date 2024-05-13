package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByNicknameAndUsername(String nickname, String username);
    //Boolean existsByUsernameAndPassword(String username, String password);
    Boolean existsByUsername(String username);

}
