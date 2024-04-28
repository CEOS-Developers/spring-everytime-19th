package com.ceos19.everyTime.member.repository;

import com.ceos19.everyTime.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findMemberByLoginId(String loginId);

    boolean existsByLoginId(String loginId);


}
