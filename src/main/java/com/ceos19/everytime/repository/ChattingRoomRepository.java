package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.ChattingRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
    @EntityGraph(attributePaths = {"participant1","participant2"})
    List<ChattingRoom> findByParticipant1Id(Long userId);
    @EntityGraph(attributePaths = {"participant1","participant2"})
    List<ChattingRoom> findByParticipant2Id(Long userId);

    @Query("SELECT c FROM ChattingRoom c " +
            "join fetch c.participant1 " +
            "join fetch c.participant2 " +
            "WHERE c.participant1.id = :userId " +
            "OR c.participant2.id = :userId ")
    List<ChattingRoom> findByParticipantId(@Param("userId") Long userId);

    @Query("SELECT c FROM ChattingRoom c " +
            "join fetch c.participant1 " +
            "join fetch c.participant2 " +
            "WHERE c.participant1.id = :user1Id AND c.participant2.id = :user2Id " +
            "OR c.participant1.id = :user2Id AND c.participant2.id = :user1Id")
    Optional<ChattingRoom> findByParticipant1IdOrParticipant2Id(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);
}
