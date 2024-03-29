package com.ceos19.springboot.school.repository;

import com.ceos19.springboot.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School,Long> {
}
