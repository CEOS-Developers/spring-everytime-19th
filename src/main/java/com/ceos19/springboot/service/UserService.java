package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.User;
import com.ceos19.springboot.exception.ErrorException;
import com.ceos19.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Logger;

import static com.ceos19.springboot.exception.ErrorCode.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            log.error("user sign in failed : already exist username");
            throw new ErrorException(DATA_ALREADY_EXIST, "username already exist");
        }

        userRepository.save(user);
        return user.getId();
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            log.error("user checking failed : username doesn't exist");
            throw new ErrorException(NO_DATA_EXIST, "username doesn't exist");
        }

        return optionalUser.get();
    }

    public void removeUser(Long userId) {
        // 연관관계 삭제 필요

        userRepository.deleteById(userId);
    }

}
