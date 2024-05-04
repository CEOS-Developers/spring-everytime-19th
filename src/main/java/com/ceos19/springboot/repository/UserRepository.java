package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(@Param("username")String username);

    Optional<Object> findOneWithAuthoritiesByUsername(String username);

//    Optional<User> findByUserId(@Param("user_id")Long userId);
}
