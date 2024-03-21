package com.ceos19.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Users {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;
    private String email;
    private String loginId;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private School university;

    @Builder
    public Users(String username,
                 String nickname,
                 School university,
                 String email,
                 String loginId,
                 String password){
        this.username = username;
        this.nickname = nickname;
        this.university = university;
        this.email = email;
        this.loginId = loginId;
        this.password = password;
    }

    public Post pressLike(Post post) {
        post.plusLike();
        return post;
    }

    public Post unlike(Post post) {
        post.minusLike();
        return post;
    }
}
