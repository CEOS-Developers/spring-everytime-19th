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
    private String username;
    private String password;
    private String nickname;
    private String email;
    private University university;

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .university(member.getUniversity())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .university(university)
                .build();
    }
}