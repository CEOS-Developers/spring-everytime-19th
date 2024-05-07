package com.ceos19.everytime.jwt.repository;

import com.ceos19.everytime.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Boolean existsByRefresh(String refresh);
    void deleteByRefresh(String refresh);
}
