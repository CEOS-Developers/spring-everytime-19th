package com.ceos19.springboot.refreshtoken.repository;

import com.ceos19.springboot.refreshtoken.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
