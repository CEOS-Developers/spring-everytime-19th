package com.ceos19.everytime.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
