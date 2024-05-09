package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class TimeTableServiceTest {
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
    @Autowired
    TimeTableService timeTableService;

    @Test
    public void 타임테이블제거시유저에서도제거되나확인() throws Exception {

        //given
        School school = new School("schoolA");
        schoolRepository.save(school);

        User userA = new User("userA@asdf.com", "password", "userA", "aaabbbc", "userA@asdf.com", school,"ROLE_ADMIN");
        userRepository.save(userA);

        Course course1 = new Course("1234-567", "컴퓨터개론", 1, "김교수", 3, "t123",school);
        Course course2 = new Course("1111-333", "엄준식개론", 3, "박교수", 2, "t123",school);

        course1.addClassTime(Weekend.MON, 2);
        course1.addClassTime(Weekend.TUE, 4);
        course1.addClassTime(Weekend.TUE, 5);
        courseRepository.save(course1);

        course2.addClassTime(Weekend.FRI, 5);
        courseRepository.save(course2);

    }

}