package com.ceos19.everytime.domain;

import com.ceos19.everytime.security.AuthMemberController;
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

    @Column(nullable = false, unique = true, length = 20)
    private String loginId;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false, unique = true, length = 10)
    private String username;

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(final String username, final String loginId, final String userPw, final String email, final University university, final Authority authority) {
        this.username = username;
        this.loginId = loginId;
        this.userPw = userPw;
        this.email = email;
        this.university = university;
        this.authority = authority;
    }

    public boolean changeUsername(final String username) {      //반환을 어떻게 해아할지?
        if(validateUsername(username) != null){
            this.username = username;
            return true;
        }
        return false;
    }

    private String validateUsername(final String username) {
        if (username.isEmpty() ||  username.length() > MAX_USERNAME_LENGTH) {
            return null;
        }
        return username;
    }

}