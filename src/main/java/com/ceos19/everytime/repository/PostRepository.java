package com.ceos19.everytime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select distinct p from Post p join fetch p.board where p.board = :board")
    List<Post> findAllFetchJoin(final Board board);
}
