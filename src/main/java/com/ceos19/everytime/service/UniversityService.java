package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.University;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UniversityService {
    public static final int MAX_NAME_LENGTH = 15;

    private final UniversityRepository universityRepository;

    @Transactional
    public Long create(String name){
        if(!validateName(name)){
            throw new CustomException(INVALID_PARAMETER);
        }

        University university = new University(name);
        return universityRepository.save(university)
                .getId();
    }

    @Transactional
    public void delete(Long universityId){
        final University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        universityRepository.delete(university);
    }

    @Transactional
    public void updateName(Long universityId, String name){
        final University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        university.changeName(name);
    }

    @Transactional
    private boolean validateName(String name){
        if(name.isEmpty() || name.length()> MAX_NAME_LENGTH)
            return false;
        return true;
    }

}
