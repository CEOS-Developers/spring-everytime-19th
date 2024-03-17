package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String loginId;

    @Column(nullable = false, length = 30)
    private String userPw;

    @Column(nullable = false, unique = true, length = 10)
    private String userName;

    @Column(nullable = false)
    private String email;


    @Builder
    public Member(final String userName, final String loginId, final String userPw, final String email) {
        this.userName = userName;
        this.loginId = loginId;
        this.userPw = userPw;
        this.email = email;
    }


}
