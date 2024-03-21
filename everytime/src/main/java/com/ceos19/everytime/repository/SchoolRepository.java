package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findByName(String name);
}
