package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Chat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @EntityGraph(attributePaths = {"author"})
    List<Chat> findByChattingRoomId(Long chattingRoomId);

    @EntityGraph(attributePaths = {"author"})
    List<Chat> findByAuthorId(Long authorID);

    @EntityGraph(attributePaths = {"author"})
    List<Chat> findBySentAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @EntityGraph(attributePaths = {"author"})
    List<Chat> findByChattingRoomIdAndSentAtBetween(Long chattingRoomId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    void deleteAllByAuthorId(Long authorId);

    void deleteAllByChattingRoomId(Long chattingRoomId);
}
