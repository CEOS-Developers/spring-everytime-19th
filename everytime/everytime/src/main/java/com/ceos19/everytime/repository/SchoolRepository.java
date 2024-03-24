package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.AboutUser.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School,Long> {
}
