package com.ceos19.springboot.commentlike.repository;

import com.ceos19.springboot.commentlike.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
