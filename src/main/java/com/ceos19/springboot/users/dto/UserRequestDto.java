package com.ceos19.springboot.users.dto;

import lombok.Getter;

@Getter
public class UserRequestDto {
    private String loginId;
    private String password;
    private String nickname;
}
