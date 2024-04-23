package com.ceos19.everytime.security;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    private String username;

    private String expiredAt;

    public RefreshToken(final String refreshToken, final String username, final String expiredAt) {
        this.refreshToken = refreshToken;
        this.username = username;
        this.expiredAt = expiredAt;
    }
}
