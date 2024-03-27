package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.School;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SchoolService {
    private final SchoolRepository schoolRepository;

    public Long addSchool(School school) {
        Optional<School> optionalSchool = schoolRepository.findByName(school.getName());
        if (optionalSchool.isPresent()) {
            log.error("에러 내용: 학교 등록 실패 " +
                    "발생 원인: 이미 존재하는 이름으로 학교 등록 시도");
            throw new AppException(DATA_ALREADY_EXISTED, "이미 존재하는 학교명입니다");
        }
        schoolRepository.save(school);
        return school.getId();
    }

    public School findSchoolById(Long schoolId) {
        Optional<School> optionalSchool = schoolRepository.findById(schoolId);
        if (optionalSchool.isEmpty()) {
            log.error("에러 내용: 학교 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
        }

        return optionalSchool.get();
    }

    public School findSchoolByName(String name) {
        Optional<School> optionalSchool = schoolRepository.findByName(name);
        if (optionalSchool.isEmpty()) {
            log.error("에러 내용: 학교 조회 실패 " +
                    "발생 원인: 존재하지 않는 학교명으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
        }
        return optionalSchool.get();
    }
}
