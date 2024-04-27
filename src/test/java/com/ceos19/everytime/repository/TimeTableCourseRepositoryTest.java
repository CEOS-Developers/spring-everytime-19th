package com.ceos19.everytime.repository;

import static com.ceos19.everytime.domain.Semester.FIRST;
import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everytime.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class TimeTableCourseRepositoryTest {
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TimeTableRepository timeTableRepository;
    @Autowired
    EntityManager em;
    @Autowired
    TimeTableCourseRepository timeTableCourseRepository;

    School school;
    User userA;
    Course course1;
    Course course2;
    TimeTable timeTable;

    @BeforeEach
    public void each() {
        school = new School("schoolA");
        schoolRepository.save(school);

        userA = new User("userA@asdf.com", "password", "userA", "aaabbbc", "userA@asdf.com", school,"ROLE_ADMIN");
        userRepository.save(userA);

        course1 = new Course("1234-567", "컴퓨터개론", 1, "김교수", 3, "t123", school);
        course2 = new Course("1111-333", "엄준식개론", 3, "박교수", 2, "t123", school);

        course1.addClassTime(Weekend.MON, 2);
        course1.addClassTime(Weekend.TUE, 4);
        course1.addClassTime(Weekend.TUE, 5);
        courseRepository.save(course1);

        course2.addClassTime(Weekend.FRI, 5);
        courseRepository.save(course2);

//        course.getClassTimes().remove(0);

        timeTable = new TimeTable("timeTable1", 2024, FIRST, userA);
        timeTableRepository.save(timeTable);

        timeTableCourseRepository.save(new TimeTableCourse(timeTable, course1));
        timeTableCourseRepository.save(new TimeTableCourse(timeTable, course2));
    }
}