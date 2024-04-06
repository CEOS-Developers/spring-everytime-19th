package com.ceos19.springboot.comment.repository;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByContent(String content);

    List<Comment> findByPost(Post post);

    void deleteByPost(Post post);
    void deleteByUser(Users user);
}
