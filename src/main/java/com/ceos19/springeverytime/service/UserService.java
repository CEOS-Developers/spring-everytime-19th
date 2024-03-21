package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Long register(User user) {
        userRepository.save(user);
        return user.getUserId();
    }

    public User findOne(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isPresent()) return findUser.get();
        throw new IllegalArgumentException("잘못된 유저 ID 입니다.");
    }
}
