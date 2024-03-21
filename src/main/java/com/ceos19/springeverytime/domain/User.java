package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    @NonNull
    @Column(length = 30, nullable = false, name = "id")
    private String loginId;

    @NonNull
    @Column(length = 30, nullable = false)
    private String pw;

    @NonNull
    @Column(length = 30, nullable = false)
    private String nickname;

    @NonNull
    @Column(length = 10, nullable = false)
    private String name;

    @NonNull
    @Column(length = 30, nullable = false)
    private String major;

    @NonNull
    @Column(length = 2, nullable = false)
    private String admissionYear;

    @NonNull
    @Column(length = 30, nullable = false)
    private String email;

    @NonNull
    @Column(nullable = false)
    private boolean isEnrolled;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createDate;
}
