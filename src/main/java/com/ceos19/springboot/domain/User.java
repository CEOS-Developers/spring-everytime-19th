package com.ceos19.springboot.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.Timestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
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

    @Column(nullable = false, length = 20)
    private String email;

    @Column(nullable = false)
    private Boolean isBoardManager;

    @Column(nullable = true, length = 20)
    private String board;

    @Column(nullable = false)
    private Boolean isBanned;

    @Column(nullable = true, length = 20)
    private LocalDateTime createdAt;

    @Column(nullable = true, length = 20)
    private LocalDateTime lastLogin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

}
