package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Boolean existsByRefresh(String refresh);
    void deleteByRefresh(String refresh);
}
