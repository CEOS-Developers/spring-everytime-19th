package com.ceos19.springeverytime.domain.chatroom.repository;

import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import com.ceos19.springeverytime.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select r from ChatRoom r where r.member1.userId = :userId or r.member2.userId = :userId")
    List<ChatRoom> findAllByUserId(@Param("userId") Long userId);

    @Query("select r from ChatRoom r where r.member1 = :user1 and r.member2 = :user2")
    Optional<ChatRoom> findChatRoomByUser1AndUser2(@Param("user1") User user1, @Param("user2") User user2);
}
