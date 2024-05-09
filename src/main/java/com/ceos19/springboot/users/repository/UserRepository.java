package com.ceos19.springboot.users.repository;

import com.ceos19.springboot.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByNickname(String nickname);
    Optional<Users> findByLoginId(String loginId);
}
