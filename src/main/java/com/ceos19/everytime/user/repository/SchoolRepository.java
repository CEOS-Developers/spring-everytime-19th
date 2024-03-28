package com.ceos19.everytime.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.user.domain.School;

public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findByNameAndDepartment(final String schoolName, final String department);
}
