package com.ceos19.everytime.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.comment.domain.Comment;
import com.ceos19.everytime.post.domain.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(final Post post);
}
