package com.ceos19.everytime.dto.member;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.University;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String loginId;
    private String userPw;
    private String username;
    private String email;
    private University university;

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .userPw(member.getUserPw())
                .username(member.getUsername())
                .email(member.getEmail())
                .university(member.getUniversity())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .loginId(loginId)
                .userPw(userPw)
                .username(username)
                .email(email)
                .university(university)
                .build();
    }
}