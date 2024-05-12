package com.ceos19.everytime.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.global.exception.BadRequestException;
import com.ceos19.everytime.global.exception.ExceptionCode;
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
    private final PasswordEncoder passwordEncoder;

    public void saveUser(final UserSaveRequestDto request) {
        final School school = schoolRepository.findByNameAndDepartment(request.schoolName(), request.department())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_SCHOOL));
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException(ExceptionCode.ALREADY_EXIST_USERNAME);
        }
        final User user = request.toEntity(school, passwordEncoder);
        userRepository.save(user);
    }

    public void deleteUser(final Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));
        userRepository.delete(user);
    }
}
