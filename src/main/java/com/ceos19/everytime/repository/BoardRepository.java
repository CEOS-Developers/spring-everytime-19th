package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findById(Long boardId);

    List<Board> findBySchoolId(Long schoolId);
    Optional<Board> findBySchoolIdAndName(Long schoolId,String name);
}
