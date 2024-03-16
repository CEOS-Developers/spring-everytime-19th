package com.ceos19.springboot.lecture.repository;

import com.ceos19.springboot.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
