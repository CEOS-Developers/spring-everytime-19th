package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u " +
            "join fetch u.school s")
    List<User> findAll();

    @Query("select u from User u " +
            "join fetch u.school s " +
            "where u.id = :userId")
    Optional<User> findById(@Param("userId") long userId);

    @Query("select u from User u " +
            "join fetch u.school s " +
            "where u.name = :name")
    Optional<User> findByName(@Param("name")String name);

    @Query("select u from User u " +
            "join fetch u.school s " +
            "where u.username = :username")
    Optional<User> findByUsername(@Param("username")String username);
}
