package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.post = :post")
    public List<Comment> findAllByPost(@Param("post") Post post);
}
