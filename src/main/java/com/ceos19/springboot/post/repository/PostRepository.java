package com.ceos19.springboot.post.repository;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByPostId(Long postId);

    @Query("select c from Comment c left join fetch c.post")
    List<Comment> findCommentFetchJoin();
}
