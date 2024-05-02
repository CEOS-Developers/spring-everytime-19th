package com.ceos19.everytime.security;

import com.ceos19.everytime.domain.Authority;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.University;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    private String loginId;
    private String userPw;
    private String username;
    private String email;
    //private University university;
    //private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, Authority authority) {

        return Member.builder()
                .loginId(loginId)
                .userPw(encodedPassword)
                .username(username)
                .email(email)
                //.university(university)
                //.authoity(authority)
                .build();
    }
}
