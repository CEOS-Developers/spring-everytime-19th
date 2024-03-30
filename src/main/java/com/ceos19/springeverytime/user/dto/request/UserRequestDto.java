package com.ceos19.springeverytime.user.dto.request;

import com.ceos19.springeverytime.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {
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
