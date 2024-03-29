package com.ceos19.everytime.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.global.exception.BadRequestException;
import com.ceos19.everytime.global.exception.NotFoundException;
import com.ceos19.everytime.user.domain.School;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.user.dto.request.UserSaveRequestDto;
import com.ceos19.everytime.user.repository.SchoolRepository;
import com.ceos19.everytime.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    public void saveUser(final UserSaveRequestDto request) {
        final School school = schoolRepository.findByNameAndDepartment(request.schoolName(), request.department())
                .orElseThrow(() -> new NotFoundException("해당 학교가 존재하지 않습니다."));
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("이미 존재하는 사용자입니다.");
        }
        final User user = request.toEntity(school);
        userRepository.save(user);
    }

    public void deleteUser(final Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User not found: %d", userId)));
        userRepository.delete(user);
    }
}
