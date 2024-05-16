package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{

    public static final int MIN_USERNAME_LENGTH = 1;
    public static final int MAX_USERNAME_LENGTH = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)       // 로그인 id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 10)       // 회원 별명
    private String nickname;

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(final String nickname, final String username, final String password, final String email, final University university, final Authority authority) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.university = university;
        this.authority = authority;
    }

    public void changeNickname(final String nickname) {
        if(validateNickname(nickname) != null){
            this.nickname = nickname;
        }
    }

    private String validateNickname(final String nickname) {
        if (nickname.isEmpty() ||  nickname.length() > MAX_USERNAME_LENGTH) {
            return null;
        }
        return nickname;
    }

}