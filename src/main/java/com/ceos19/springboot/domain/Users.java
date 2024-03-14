package com.ceos19.springboot.domain;

import com.ceos19.springboot.repository.UserRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Users {

    @Id
    @GeneratedValue
    private UUID userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String university;
    private String nickname;
    private String email;
    private String loginId;
    private String password;

    @Builder
    public Users(String username,
                 String university,
                 String nickname,
                 String email,
                 String loginId,
                 String password){
        this.username = username;
        this.university = university;
        this.nickname = nickname;
        this.email = email;
        this.loginId = loginId;
        this.password = password;
    }
}
