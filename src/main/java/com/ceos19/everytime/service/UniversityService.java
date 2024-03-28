package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.University;
import com.ceos19.everytime.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UniversityService {
    public static final int MAX_NAME_LENGTH = 15;

    private final UniversityRepository universityRepository;

    public University create(String name){
        if(!validateName(name)){
            log.info("[Service][create] FAIL");
            return null;
        }

        University university = new University(name);
        universityRepository.save(university);
        log.info("[Service][create] SUCCESS");
        return university;
    }

    public void delete(Long universityId){
        Optional<University> university = universityRepository.findById(universityId);

        if(university.isEmpty()){
            log.info("[Service][delete] FAIL");
        }
        else{
            universityRepository.delete(university.get());
            log.info("[Service][delete] SUCCESS");
        }
    }

    public void updateName(Long universityId, String name){
        Optional<University> university = universityRepository.findById(universityId);

        if(university.isEmpty() || !validateName(name)){
            log.info("[Service][updateName] FAIL");
        }
        else{
            university.get().changeName(name);
            log.info("[Service][updateName] SUCCESS");
        }

    }

    private boolean validateName(String name){
        if(name.isEmpty() || name.length()> MAX_NAME_LENGTH)
            return false;
        return true;
    }

}
