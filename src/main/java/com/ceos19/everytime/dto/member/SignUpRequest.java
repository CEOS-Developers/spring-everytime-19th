package com.ceos19.everytime.dto.member;

import com.ceos19.everytime.domain.Authority;
import com.ceos19.everytime.domain.Member;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    private String username;
    private String password;
    private String nickname;
    private String email;
    //private University university;

    public Member toEntity(String encodedPassword, Authority authority) {

        return Member.builder()
                .username(nickname)
                .password(encodedPassword)
                .nickname(nickname)
                .email(email)
                //.university(university)
                .authority(authority)
                .build();
    }

}
