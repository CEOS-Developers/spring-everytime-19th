package com.ceos19.springeverytime.domain.user.service;

import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User register(User user) {
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
        Optional<User> findUser = userRepository.findByLoginId(user.getLoginId());
        if (findUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원 아이디입니다.");
        }
    }
}
