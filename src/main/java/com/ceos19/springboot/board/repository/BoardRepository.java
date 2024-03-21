package com.ceos19.springboot.board.repository;

import com.ceos19.springboot.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
