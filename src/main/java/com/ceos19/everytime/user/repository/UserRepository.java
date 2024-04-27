package com.ceos19.everytime.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(final String username);

    Optional<User> findByUsername(final String username);
}
