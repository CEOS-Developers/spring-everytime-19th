package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByParentComment(Comment parentComment);

}
