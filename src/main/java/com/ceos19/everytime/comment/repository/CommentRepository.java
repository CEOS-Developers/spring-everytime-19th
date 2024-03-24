package com.ceos19.everytime.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
