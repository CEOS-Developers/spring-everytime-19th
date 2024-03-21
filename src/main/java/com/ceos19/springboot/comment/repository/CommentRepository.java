package com.ceos19.springboot.comment.repository;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
