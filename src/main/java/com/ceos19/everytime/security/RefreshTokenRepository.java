package com.ceos19.everytime.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByRefreshToken(String refresh);

    void deleteByRefreshToken(String refresh);
}
