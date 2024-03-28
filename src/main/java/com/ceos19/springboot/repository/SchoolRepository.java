package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School,Long> {
}
