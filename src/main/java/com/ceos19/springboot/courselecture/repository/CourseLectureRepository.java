package com.ceos19.springboot.courselecture.repository;

import com.ceos19.springboot.courselecture.entity.CourseLecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseLectureRepository extends JpaRepository<CourseLecture, Long> {
}
