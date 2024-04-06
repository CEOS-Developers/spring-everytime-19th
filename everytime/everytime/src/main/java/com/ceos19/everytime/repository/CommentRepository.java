package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.AboutPost.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
