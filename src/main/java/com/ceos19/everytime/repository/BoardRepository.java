package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByIdAndMemberId(Long id, Long memberId);
}
