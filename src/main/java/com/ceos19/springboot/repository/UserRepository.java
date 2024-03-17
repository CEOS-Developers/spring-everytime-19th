package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    List<User> findAll();

//    Optional<User> findById(@Param("userId") long userId);
//
//    Optional<User> findByName(@Param("name")String name);

    Optional<User> findByUsername(@Param("username")String username);
}
