package com.ceos19.springeverytime.user.service;

import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.dto.request.UserJoinDto;
import com.ceos19.springeverytime.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    @Transactional
    public void join(UserJoinDto userJoinDto) {
        if (userRepository.existsUserByLoginId(userJoinDto.getId())) {
            throw new IllegalStateException("User already exists");
        }

        User user = userJoinDto.toEntity();
        userRepository.save(user);
    }

    public User getUser(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(IllegalStateException::new);
    }
}
