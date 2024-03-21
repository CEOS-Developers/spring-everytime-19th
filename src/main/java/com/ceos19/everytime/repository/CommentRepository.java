package com.ceos19.everytime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
