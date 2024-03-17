package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
