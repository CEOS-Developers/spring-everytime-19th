package com.ceos19.springboot.tablecourse.repository;

import com.ceos19.springboot.tablecourse.entity.TableCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableCourseRepository extends JpaRepository<TableCourse, Long> {
}
