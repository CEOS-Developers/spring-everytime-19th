package com.ceos19.springboot.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 12)
    private String password;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false)
    private Boolean isAdmin;

    @Column(nullable = false, length = 20)
    private String userLast;

    @Column(nullable = false, length = 20)
    private String userFirst;

    @Email
    @Column(nullable = false, length = 20)
    private String email;

    @Column(nullable = false)
    private Boolean isBanned;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "school_id")
    private School school;

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;


    @Builder
    public User(String username, String password, String nickname,
                  Boolean isAdmin, String userLast, School school,
                  String userFirst, String email, Boolean isBanned,
                  Boolean activated, Set<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.isAdmin = isAdmin;
        this.userLast = userLast;
        this.userFirst = userFirst;
        this.email = email;
        this.isBanned = isBanned;
        this.school = school;
        this.activated = activated;
        this.authorities = authorities;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
