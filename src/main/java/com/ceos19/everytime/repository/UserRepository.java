package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.School;
import com.ceos19.everytime.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"school"})
    Optional<User> findById(long userId);

    @EntityGraph(attributePaths = {"school"})
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"school"})
    Optional<User> findByUsernameAndPassword(String username, String password);

    @EntityGraph(attributePaths = {"school"})
    Optional<User> findBySchoolIdAndStudentNo(long schoolId, String studentNo);

    @EntityGraph(attributePaths = {"school"})
    Optional<User> findByEmail(String email);

    List<User> findBySchoolId(Long schoolId);

    @EntityGraph(attributePaths = {"school"})
    List<User> findByName(String name);
}
