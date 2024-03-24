package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
