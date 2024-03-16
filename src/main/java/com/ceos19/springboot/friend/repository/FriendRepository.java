package com.ceos19.springboot.friend.repository;

import com.ceos19.springboot.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
