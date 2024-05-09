package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.Authority;
import com.ceos19.springboot.domain.User;
import com.ceos19.springboot.dto.UserDto;
import com.ceos19.springboot.exception.ErrorException;
import com.ceos19.springboot.repository.UserRepository;
import com.ceos19.springboot.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.ceos19.springboot.exception.ErrorCode.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Bean
    public static PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public User signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        // 가입되어 있지 않은 회원이면,
        // 권한 정보 만들고
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        // 유저 정보를 만들어서 save
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .isAdmin(userDto.getIsAdmin())
                .userLast(userDto.getUserLast())
                .userFirst(userDto.getUserFirst())
                .email(userDto.getEmail())
                .isBanned(userDto.getIsBanned())
                .school(userDto.getSchool())
                .activated(true)
                .authorities(Collections.singleton(authority))
                .build();

        return userRepository.save(user);
    }

    // 유저,권한 정보를 가져오는 메소드
    @Transactional(readOnly = true)
    public Optional<Object> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    // 현재 securityContext에 저장된 username의 정보만 가져오는 메소드
    @Transactional(readOnly = true)
    public Optional<Object> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }


}
