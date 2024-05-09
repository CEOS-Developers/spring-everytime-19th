package com.ceos19.everytime.jwt.repository;

import com.ceos19.everytime.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefresh(String refresh);
    Boolean existsByRefresh(String refresh);
    void deleteByRefresh(String refresh);
}
