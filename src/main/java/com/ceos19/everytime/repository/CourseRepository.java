package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    @EntityGraph(attributePaths = {"classTimes"})
    Optional<Course> findById(long courseId);

    @EntityGraph(attributePaths = {"classTimes"})
    List<Course> findByCourseNumber(String courseNumber);

    @EntityGraph(attributePaths = {"classTimes"})
    List<Course> findBySchoolIdAndName(Long schoolId, String name);

    @EntityGraph(attributePaths = {"classTimes"})
    List<Course> findBySchoolIdAndOpeningGrade(Long schoolId, int openingGrade);

    @EntityGraph(attributePaths = {"classTimes"})
    List<Course> findBySchoolIdAndProfessorName(Long schoolId, String professorName);

    @EntityGraph(attributePaths = {"classTimes"})
    List<Course> findBySchoolIdAndNameAndProfessorName(Long schoolId, String name, String professorName);

    @EntityGraph(attributePaths = {"classTimes"})
    List<Course> findBySchoolIdAndCourseNumber(Long schoolId, String courseNo);
}
