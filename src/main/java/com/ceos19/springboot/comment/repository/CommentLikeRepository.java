package com.ceos19.springboot.comment.repository;

import com.ceos19.springboot.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<Comment,Long> {
}
