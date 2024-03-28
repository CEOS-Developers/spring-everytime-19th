package com.ceos19.everytime.user.dto.request;

import com.ceos19.everytime.user.domain.School;
import com.ceos19.everytime.user.domain.User;

public record UserSaveRequestDto(String username, String password, String nickname, String schoolName, String department) {
    public User toEntity(final School school) {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .school(school)
                .build();
    }
}
