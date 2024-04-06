package com.ceos19.springeverytime.domain.comment.repository;

import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    boolean existsByAuthorUserIdAndCommentId(Long userId, Long id);

    @Query("select c from Comment c where c.post = :post")
    List<Comment> findAllByPost(@Param("post") Post post);
}
