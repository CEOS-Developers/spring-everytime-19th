package com.ceos19.springeverytime.user.repository;

import com.ceos19.springeverytime.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByLoginId(String loginId);

    Optional<User> findUserById(Long userId);
}
