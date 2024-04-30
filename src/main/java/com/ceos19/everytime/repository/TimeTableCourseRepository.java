package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Semester;
import com.ceos19.everytime.domain.TimeTableCourse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Jpa21Utils;

import java.util.List;
import java.util.Optional;

public interface TimeTableCourseRepository extends JpaRepository<TimeTableCourse,Long> {
    @EntityGraph(attributePaths = {"timeTable"})
    List<TimeTableCourse> findByCourseId(Long courseId);  // 특정 Course로 조회, 이걸 쓸지는 잘 모르겠다..

    @EntityGraph(attributePaths = {"course"})
    List<TimeTableCourse> findByTimeTableId(Long timeTableId);  // 특정 TimeTable로 조회

    @EntityGraph(attributePaths = {"timeTable","course"})
    Optional<TimeTableCourse> findByTimeTableIdAndCourseId(Long timeTableId, Long courseId);

    void deleteAllByTimeTableId(Long timeTableId);

    void deleteAllByCourseId(Long courseId);
}
