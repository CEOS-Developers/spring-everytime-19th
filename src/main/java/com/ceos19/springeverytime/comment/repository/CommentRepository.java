package com.ceos19.springeverytime.comment.repository;

import com.ceos19.springeverytime.comment.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByUserId(Long userId);

    List<Comment> findCommentsByPostId(Long postId);

    Optional<Comment> findCommentById(Long id);

}
