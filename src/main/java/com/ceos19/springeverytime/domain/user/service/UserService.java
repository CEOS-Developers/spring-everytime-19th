package com.ceos19.springeverytime.domain.user.service;

import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.user.dto.request.UserCreateRequest;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import com.ceos19.springeverytime.global.exception.BadRequestException;
import com.ceos19.springeverytime.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User register(UserCreateRequest request) {
        User user = new User(
                request.getLoginId(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname(),
                request.getName(),
                request.getMajor(),
                request.getAdmissionYear(),
                request.getEmail()
        );
        validateDuplicatedUser(user);
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User findOne(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isPresent()) return findUser.get();
        throw new IllegalArgumentException("잘못된 유저 ID 입니다.");
    }

    private void validateDuplicatedUser(User user) {
        if (userRepository.existsByLoginId(user.getLoginId())) {
            throw new BadRequestException(ExceptionCode.DUPLICATED_LOGIN_ID);
        }
    }
}
