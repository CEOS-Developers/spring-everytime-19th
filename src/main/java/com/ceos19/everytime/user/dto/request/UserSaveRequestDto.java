package com.ceos19.everytime.user.dto.request;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ceos19.everytime.user.domain.School;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.user.domain.UserRole;

public record UserSaveRequestDto(String username, String password, String nickname, String schoolName, String department) {
    public User toEntity(final School school, final PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .role(UserRole.USER)
                .school(school)
                .build();
    }
}
