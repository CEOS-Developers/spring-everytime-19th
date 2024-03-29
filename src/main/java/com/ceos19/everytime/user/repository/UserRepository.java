package com.ceos19.everytime.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(final String username);
}
