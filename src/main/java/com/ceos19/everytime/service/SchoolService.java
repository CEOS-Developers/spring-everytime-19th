package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.School;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SchoolService {
    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = false)
    public Long addSchool(String schoolName) {
        School school = new School(schoolName);
        schoolRepository.findByName(schoolName).ifPresent(
                sc -> {
                    log.error("에러 내용: 학교 등록 실패 " +
                            "발생 원인: 이미 존재하는 이름으로 학교 등록 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 존재하는 학교명입니다");
                });
        schoolRepository.save(school);
        return school.getId();
    }

    public School findSchoolById(Long schoolId) {
        return schoolRepository.findById(schoolId)
                .orElseThrow(() -> {
                    log.error("에러 내용: 학교 조회 실패 " +
                            "발생 원인: 존재하지 않는 PK 값으로 조회");
                    return new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
                });
    }

    public School findSchoolByName(String name) {
        return schoolRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("에러 내용: 학교 조회 실패 " +
                            "발생 원인: 존재하지 않는 학교명으로 조회");
                    return new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
                });
    }

    public List<School> findSchools() {
        return schoolRepository.findAll();
    }

    @Transactional(readOnly = false)
    public void modifySchool(Long schoolId, String name) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> {
                    log.error("에러 내용: 학교 조회 실패 " +
                            "발생 원인: 존재하지 않는 PK 값으로 조회");
                    return new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
                });

        schoolRepository.findByName(name)
                .ifPresent(s -> {
                    log.error("에러 내용: 학교 이름 변경 실패 " +
                            "발생 원인: 이미 사용 중인 학교 명으로 수정 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 사용 중인 학교명입니다");
                });

        school.updateName(name);
    }

}
