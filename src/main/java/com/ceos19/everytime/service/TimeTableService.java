package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
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

    public Long save(TimeTable timeTable) {
        List<TimeTable> timeTables = timeTableRepository.findByUserId(timeTable.getUser().getId());
        if (timeTableRepository.findByUserIdAndYearAndSemesterAndName
                (timeTable.getUser().getId(),
                        timeTable.getYear(),
                        timeTable.getSemester(),
                        timeTable.getName())
                .isPresent())
        {
            log.error("에러 내용: 시간표 생성 불가 " +
                    "발생 원인: 중복된 시간표 등록");
            throw new AppException(DATA_ALREADY_EXISTED, "중복된 시간표입니다");
        }

        timeTableRepository.save(timeTable);
        return timeTable.getId();
    }

    @Transactional(readOnly = true)
    public TimeTable findById(Long timeTableId) {
        Optional<TimeTable> optionalTimeTable = timeTableRepository.findById(timeTableId);
        if (optionalTimeTable.isEmpty()) {
            log.error("에러 내용: 시간표 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 시간표입니다");
        }
        return optionalTimeTable.get();
    }

    @Transactional(readOnly = true)
    public List<TimeTable> findByUserId(Long userId) {
        return timeTableRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<TimeTable> findByUserIdAndYearAndSemester(Long userId, int year, Semester semester) {
        return timeTableRepository.findByUserIdAndYearAndSemester(userId, year, semester);
    }

    @Transactional(readOnly = true)
    public TimeTable findByUserIdAndYearAndSemesterAndName(Long userId, int year, Semester semester, String name) {
        Optional<TimeTable> optionalTimeTable = timeTableRepository.findByUserIdAndYearAndSemesterAndName(userId, year, semester, name);
        if (optionalTimeTable.isEmpty()) {
            log.error("에러 내용: 시간표 조회 실패 " +
                    "발생 원인: 조건에 맞는 시간표 존재 안함");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 시간표입니다");
        }
        return optionalTimeTable.get();
    }


    public void deleteTimeTable(Long timeTableId) {
        Optional<TimeTable> optionalTimeTable = timeTableRepository.findById(timeTableId);
        if (optionalTimeTable.isEmpty()) {
            log.error("에러 내용: 시간표 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 시간표입니다");
        }

        // 연관관계 제거
        timeTableCourseRepository.deleteAllByTimeTableId(timeTableId);

        // timeTable 제거
        timeTableRepository.deleteById(timeTableId);
    }

}
