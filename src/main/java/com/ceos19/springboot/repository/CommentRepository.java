package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.Comment;
import com.ceos19.springboot.domain.Post;
import com.ceos19.springboot.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByContent(String content);

    void deleteByPost(Post post);
    void deleteByUser(Users user);
}
