package com.ceos19.springboot.friend.repository;

import com.ceos19.springboot.friend.entity.Friend;
import com.ceos19.springboot.user.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @EntityGraph(attributePaths = {"user_first", "user_second"})
    @Query("SELECT f FROM Friend f WHERE ((f.user1 = :user_first AND f.user2 = :user_second) OR (f.user1 = :user_second AND f.user2 = :user_first))")
    Optional<Friend> findByUser1AndUser2(@Param("user_first") User user_first, @Param("user_second") User user_second);
}
