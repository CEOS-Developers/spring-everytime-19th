package com.ceos19.everytime.dto.member;

import com.ceos19.everytime.domain.Authority;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.University;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    private String loginId;
    private String userPw;
    private String username;
    private String email;
    private University university;

    public Member toEntity(String encodedPassword, Authority authority) {

        return Member.builder()
                .loginId(loginId)
                .userPw(encodedPassword)
                .username(username)
                .email(email)
                .university(university)
                .authority(authority)
                .build();
    }

}
