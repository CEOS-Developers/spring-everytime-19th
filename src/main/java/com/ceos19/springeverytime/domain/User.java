package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class User {
    @Id
    @Column(length = 30, nullable = false)
    String id;

    @Column(length = 30, nullable = false)
    String pw;

    @Column(length = 30, nullable = false)
    String nickname;

    @Column(length = 10, nullable = false)
    String name;

    @Column(length = 30, nullable = false)
    String major;

    @Column(length = 2, nullable = false)
    String admissionYear;

    @Column(length = 30, nullable = false)
    String email;

    @Column(nullable = false)
    boolean isEnrolled;

    @Temporal(TemporalType.TIMESTAMP)
    Date createDate;
}
