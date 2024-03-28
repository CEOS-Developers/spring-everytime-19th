package com.ceos19.everytime.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.user.domain.School;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.user.dto.request.UserSaveRequestDto;
import com.ceos19.everytime.user.repository.SchoolRepository;
import com.ceos19.everytime.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    @Transactional
    public void saveUser(final UserSaveRequestDto request) {
        final School school = schoolRepository.findByNameAndDepartment(request.schoolName(), request.department())
                .orElseThrow(() -> new IllegalArgumentException("해당 학교가 존재하지 않습니다."));
        final User user = request.toEntity(school);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(final Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User not found: %d", userId)));
        userRepository.delete(user);
    }
}
