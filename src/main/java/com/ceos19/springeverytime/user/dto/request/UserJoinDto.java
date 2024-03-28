package com.ceos19.springeverytime.user.dto.request;

import com.ceos19.springeverytime.user.domain.User;
import lombok.Data;

@Data
public class UserJoinDto {
    private String id;
    private String password;
    private String name;

    public User toEntity(){
        return User.builder()
                .loginId(id)
                .password(password)
                .name(name)
                .build();
    }
}
