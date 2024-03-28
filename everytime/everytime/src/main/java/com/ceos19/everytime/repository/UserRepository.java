package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.AboutUser.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("select distinct u from User u join fetch u.school")
    List<User> findUserFetchJoin();

    @EntityGraph(attributePaths = {"school"})
    @Query("select u from User u")
    List<User> findUserEntityGraph();
}
