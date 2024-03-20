package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Long register(User user) {
        userRepository.save(user);
        return user.getUserId();
    }

    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }
}
