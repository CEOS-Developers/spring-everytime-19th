package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByIdAndBoardManager(Long id, Member boardManager);
}
