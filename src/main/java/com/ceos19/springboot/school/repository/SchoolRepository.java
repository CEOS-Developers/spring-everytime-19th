package com.ceos19.springboot.school.repository;

import com.ceos19.springboot.school.entity.School;
import com.ceos19.springboot.tablecourse.entity.TableCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}