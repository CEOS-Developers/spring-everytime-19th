package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
