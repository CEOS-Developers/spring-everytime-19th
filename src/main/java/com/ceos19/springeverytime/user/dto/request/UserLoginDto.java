package com.ceos19.springeverytime.user.dto.request;

import com.ceos19.springeverytime.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLoginDto {
    private final String loginId;
    private final String password;

    public User toEntity(){
        return User.builder()
                .loginId(loginId)
                .password(password)
                .build();
    }
}
