package com.ceos19.everytime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
