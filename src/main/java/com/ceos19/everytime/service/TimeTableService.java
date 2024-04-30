package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.dto.AddTimeTableRequest;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.repository.CourseRepository;
import com.ceos19.everytime.repository.TimeTableCourseRepository;
import com.ceos19.everytime.repository.TimeTableRepository;
import com.ceos19.everytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TimeTableService {
    private final TimeTableRepository timeTableRepository;
    private final TimeTableCourseRepository timeTableCourseRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public Long addTimeTable(TimeTable timeTable) {
        timeTableRepository.findByUserIdAndYearAndSemesterAndName
                        (timeTable.getUser().getId(),
                                timeTable.getYear(),
                                timeTable.getSemester(),
                                timeTable.getName())
                .ifPresent(f -> {
                    log.error("에러 내용: 시간표 생성 불가 " +
                            "발생 원인: 중복된 시간표 등록");
                    throw new AppException(DATA_ALREADY_EXISTED, "중복된 시간표입니다");
                });


        timeTableRepository.save(timeTable);
        return timeTable.getId();
    }

    public Long addTimeTable(Long userId, AddTimeTableRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("에러 내용: 시간표 생성 불가 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
        timeTableRepository.findByUserIdAndYearAndSemesterAndName(userId,
                        request.getYear(),
                        request.getSemester(),
                        request.getName())
                .ifPresent(f -> {
                    log.error("에러 내용: 시간표 생성 불가 " +
                            "발생 원인: 중복된 시간표 등록");
                    throw new AppException(DATA_ALREADY_EXISTED, "중복된 시간표입니다");
                });

        TimeTable timeTable = new TimeTable(request.getName(), request.getYear(), request.getSemester(), user);

        timeTableRepository.save(timeTable);
        return timeTable.getId();
    }

    public Long addCourseToTimeTable(Long timeTableId, Long courseId) {
        TimeTable timeTable = timeTableRepository.findById(timeTableId).orElseThrow(() -> {
            log.error("에러 내용: 수업 등록 실패 " +
                    "발생 원인: 존재하지 않는 TimeTable의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 시간표입니다");
        });
        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            log.error("에러 내용: 수업 등록 실패 " +
                    "발생 원인: 존재하지 않는 Course의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 수업입니다");
        });
        timeTableCourseRepository.findByTimeTableIdAndCourseId(timeTableId, courseId).ifPresent(f -> {
            log.error("에러 내용: 수업 등록 실패 " +
                    "발생 원인: 시간표에 이미 등록된 수업을 등록 시도");
            throw new AppException(DATA_ALREADY_EXISTED, "이미 등록된 수업입니다");
        });

        TimeTableCourse timeTableCourse = new TimeTableCourse(timeTable, course);
        timeTableCourseRepository.save(timeTableCourse);

        return timeTableCourse.getId();
    }

    @Transactional(readOnly = true)
    public TimeTable findTimeTableById(Long timeTableId) {
        return timeTableRepository.findById(timeTableId).orElseThrow(() -> {
            log.error("에러 내용: 시간표 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 시간표입니다");
        });
    }

    @Transactional(readOnly = true)
    public List<TimeTable> findTimeTableByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("에러 내용: 시간표 조회 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });

        return timeTableRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<TimeTable> findTimeTableByUserIdAndYearAndSemester(Long userId, int year, Semester semester) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("에러 내용: 시간표 조회 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
        return timeTableRepository.findByUserIdAndYearAndSemester(userId, year, semester);
    }

    @Transactional(readOnly = true)
    public TimeTable findTimeTableByUserIdAndYearAndSemesterAndName(Long userId, int year, Semester semester, String name) {
        return timeTableRepository.findByUserIdAndYearAndSemesterAndName(userId, year, semester, name)
                .orElseThrow(() -> {
                    log.error("에러 내용: 시간표 조회 실패 " +
                            "발생 원인: 조건에 맞는 시간표 존재 안함");
                    return new AppException(NO_DATA_EXISTED, "존재하지 않는 시간표입니다");
                });
    }


    public void removeTimeTable(Long timeTableId) {
        timeTableRepository.findById(timeTableId).orElseThrow(() -> {
            log.error("에러 내용: 시간표 제거 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 시간표입니다");
        });

        // 연관관계 제거
        timeTableCourseRepository.deleteAllByTimeTableId(timeTableId);

        // timeTable 제거
        timeTableRepository.deleteById(timeTableId);
    }

    public void removeCourseFromTimeTable(Long timeTableId, Long courseId) {
        TimeTable timeTable = timeTableRepository.findById(timeTableId).orElseThrow(() -> {
            log.error("에러 내용: 수업 삭제 실패 " +
                    "발생 원인: 존재하지 않는 TimeTable의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 시간표입니다");
        });
        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            log.error("에러 내용: 수업 삭제 실패 " +
                    "발생 원인: 존재하지 않는 Course의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 수업입니다");
        });
        TimeTableCourse timeTableCourse = timeTableCourseRepository.findByTimeTableIdAndCourseId(timeTableId, courseId).orElseThrow(() -> {
            log.error("에러 내용: 수업 삭제 실패 " +
                    "발생 원인: 시간표에 등록되지 않은 수업 삭제 시도");
            return new AppException(NO_DATA_EXISTED, "해당 시간표에 등록되지 않은 수업입니다");
        });

        timeTableCourseRepository.delete(timeTableCourse);
    }

}
