package com.ceos19.springboot.users.dto;

import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.post.dto.PostResponseDto;
import com.ceos19.springboot.school.domain.School;
import com.ceos19.springboot.users.domain.Users;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private String username;
    private String nickname;
    private String email;
    private String loginId;
    private School university;

    private UserResponseDto(String username, String nickname, String email, String loginId, School university) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.loginId = loginId;
        this.university = university;
    }

    public static UserResponseDto createFromPost(Users user) {
        return new UserResponseDto(
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getLoginId(),
                user.getUniversity()
                );
    }
}
