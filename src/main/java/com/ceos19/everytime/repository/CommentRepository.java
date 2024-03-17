package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
