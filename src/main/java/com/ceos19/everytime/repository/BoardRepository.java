package com.ceos19.everytime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
