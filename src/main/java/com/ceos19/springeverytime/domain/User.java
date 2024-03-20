package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue
    private Long memberId;

    @Column(length = 30, nullable = false, name = "id")
    private String loginId;

    @Column(length = 30, nullable = false)
    private String pw;

    @Column(length = 30, nullable = false)
    private String nickname;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String major;

    @Column(length = 2, nullable = false)
    private String admissionYear;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean isEnrolled;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
}
