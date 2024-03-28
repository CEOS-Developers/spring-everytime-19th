package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Course;
import com.ceos19.everytime.domain.School;
import com.ceos19.everytime.domain.TimeTableCourse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.repository.CourseRepository;
import com.ceos19.everytime.repository.SchoolRepository;
import com.ceos19.everytime.repository.TimeTableCourseRepository;
import com.ceos19.everytime.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final SchoolRepository schoolRepository;
    private final CourseRepository courseRepository;
    private final TimeTableCourseRepository timeTableCourseRepository;

    public Long addCourse(Course course) {
        Long schoolId = course.getSchool().getId();
        String courseNumber = course.getCourseNumber();

        if (!courseRepository.findBySchoolIdAndCourseNumber(schoolId, courseNumber).isEmpty()) {
            log.error("에러 내용: 과목 등록 실패 " +
                    "발생 원인: 이미 존재하는 학수 번호로 등록 시도");
            throw new AppException(DATA_ALREADY_EXISTED, "이미 등록된 학수 번호입니다");
        }
        courseRepository.save(course);
        return course.getId();
    }

    @Transactional(readOnly = true)
    public Course findCourseById(Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            log.error("에러 내용: 과목 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 과목입니다");
        }
        return optionalCourse.get();
    }

    @Transactional(readOnly = true)
    public List<Course> findCourseByTimeTableId(Long timeTableId) {
        List<TimeTableCourse> timeTableCourses = timeTableCourseRepository.findByTimeTableId(timeTableId);

        List<Course> courses = new ArrayList<>();
        for (TimeTableCourse timeTableCourse : timeTableCourses) {
            Optional<Course> optionalCourse = courseRepository.findById(timeTableCourse.getCourse().getId());
            if (optionalCourse.isPresent()) {
                Course course = optionalCourse.get();
                courses.add(course);
            }
        }
        return courses;
    }

    @Transactional(readOnly = true)
    public List<Course> findCourseByProfessorName(Long schoolId, String professorName) {
        if (schoolRepository.findById(schoolId).isEmpty()) {
            log.error("에러 내용: 학교 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
        }
        return courseRepository.findBySchoolIdAndProfessorName(schoolId, professorName);
    }

    @Transactional(readOnly = true)
    public List<Course> findCourseByNameAndProfessorName(Long schoolId, String name, String professorName) {
        if (schoolRepository.findById(schoolId).isEmpty()) {
            log.error("에러 내용: 학교 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
        }
        return courseRepository.findBySchoolIdAndNameAndProfessorName(schoolId, name, professorName);
    }

    @Transactional(readOnly = true)
    public List<Course> findCourseByName(Long schoolId, String name) {
        if (schoolRepository.findById(schoolId).isEmpty()) {
            log.error("에러 내용: 학교 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
        }
        return courseRepository.findBySchoolIdAndName(schoolId, name);
    }


    public void removeCourseById(Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            log.error("에러 내용: 시간표 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 시간표입니다");
        }
        // 연관 관계 제거
        timeTableCourseRepository.deleteAllByCourseId(courseId);

        // Course 제거
        courseRepository.deleteById(courseId);
    }


}
