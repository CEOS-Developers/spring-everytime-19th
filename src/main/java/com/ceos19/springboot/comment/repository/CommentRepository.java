package com.ceos19.springboot.comment.repository;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select c from Comment c join fetch c.post where c.post =:post")
    List<Comment> findByPostId(Post post);
}
